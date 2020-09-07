package sven.phd.iot.predictions;
import sven.phd.iot.conflicts.ConflictSolutionManager;
import sven.phd.iot.conflicts.ConflictVerificationManager;
import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.RuleExecutionEvent;
import sven.phd.iot.hassio.updates.ImplicitBehaviorEvent;
import sven.phd.iot.rules.Action;
import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.students.mathias.states.Conflict;
import sven.phd.iot.students.mathias.states.ConflictSolution;
import sven.phd.iot.students.mathias.states.ConflictingAction;
import sven.phd.iot.students.mathias.states.SolutionExecutionEvent;

import java.util.*;

public class PredictionEngine {
    private HassioStateScheduler stateScheduler;
    private RulesManager rulesManager;
    private HassioDeviceManager hassioDeviceManager;
    private ConflictSolutionManager solutionManager;
    private ConflictVerificationManager conflictVerificationManager;
    private Future future;
    private Boolean predicting;
    private long tickRate = 5; // minutes
    private long predictionWindow = 1 * 24 * 60; // 1 day in minutes

    public PredictionEngine(RulesManager rulesManager, HassioDeviceManager hassioDeviceManager, ConflictSolutionManager solutionManager, ConflictVerificationManager conflictVerificationManager) {
        this.rulesManager = rulesManager;
        this.hassioDeviceManager = hassioDeviceManager;
        this.stateScheduler = hassioDeviceManager.getStateScheduler();
        this.solutionManager = solutionManager;
        this.conflictVerificationManager = conflictVerificationManager;
        this.future = new Future();
        this.future.setFutureConflictSolutions(solutionManager.getSolutions());
        this.predicting = false;
    }

    public Future getFuture() {
        return future;
    }

    /**
     * Predict the future with the latest information we have
     */
    public void updateFuturePredictions() {
        this.future = predictFuture(new HashMap<>(), new ArrayList<>());
    }

    /**
     * Predict an alternative future with simulated input
     * @param simulatedRulesEnabled a hashmap that holds a boolean (enabled) for every rule
     * @param simulatedStates a list of additional states
     */
    public Future whatIf(HashMap<String, Boolean> simulatedRulesEnabled, List<HassioState> simulatedStates) {
        return predictFuture(simulatedRulesEnabled, simulatedStates);
    }

    /**
     * Predict the future states and event of each HassioDevice and each rule
     * @post: Each HassioDevice and Each Rule will have a cached version of the outcome
     */
    private Future predictFuture(HashMap<String, Boolean> simulatedRulesEnabled, List<HassioState> simulatedStates) {
        Future future = new Future();

        // Initialise the queue with changes we already know
        PriorityQueue<HassioState> queue = new PriorityQueue<>();

        HashMap<String, HassioState> lastStates = hassioDeviceManager.getCurrentStates();
        queue.addAll(hassioDeviceManager.predictFutureStates());
        queue.addAll(simulatedStates);
        queue.addAll(stateScheduler.getScheduledStates());

        Date lastFrameDate = new Date(); // Prediction start
        Date predictionEnd = new Date(new Date().getTime() + getPredictionWindow() * 60L * 1000L); // Convert prediction window from minutes to milliseconds

        // Predict the first day with high precision
        while(lastFrameDate.getTime() < predictionEnd.getTime()) {
            Date nextTickDate = new Date(lastFrameDate.getTime() + (getTickRate() * 60L * 1000L)); // Convert tickRate from minutes to milliseconds

            // If there is an element in the queue that will happen before the tick
            if(!queue.isEmpty() && queue.peek().getLastChanged().getTime() < nextTickDate.getTime()) {
                nextTickDate = queue.peek().getLastChanged();
            }

            this.tick(nextTickDate, lastStates, queue, future, simulatedRulesEnabled);
            lastFrameDate = nextTickDate;
        }

        // Finish predicting the rest of the queue (within the prediction window)
        while(!queue.isEmpty()) {
            if(queue.peek().getLastChanged().getTime() < predictionEnd.getTime()) {
                this.tick(queue.peek().getLastChanged(), lastStates, queue, future, simulatedRulesEnabled);
            } else {
                queue.poll();
            }
        }

        System.out.println("Predictions updated: " + future.getFutureStates().size());

        return future;
    }

    /**
     * Simulate a tick, and resolve conflicts if needed
     * @param newDate
     * @param lastStates
     * @param globalQueue
     * @param future
     * @param simulatedRulesEnabled
     */
    private void tick(Date newDate, HashMap<String, HassioState> lastStates, PriorityQueue<HassioState> globalQueue, Future future, HashMap<String, Boolean> simulatedRulesEnabled) {
        List<ConflictingAction> snoozedActions = new ArrayList<>();
        CausalStack causalStack = new CausalStack();
        CausalLayer firstLayer = new CausalLayer(newDate); // Never changes, because no rules are executed yet
        boolean runRequired = true;

        // IMPLICIT Let the devices predict their state, based on the past states (e.g. temperature)
        if(this.isPredicting()) {
            firstLayer.addAll(this.verifyImplicitStates(newDate, lastStates));
            firstLayer.addAll(this.verifyImplicitRules(newDate, lastStates));
        }

        // BASELINE: Add states from the global queue (before the current date), which could induce conflicts
        while(!globalQueue.isEmpty() && globalQueue.peek().getLastChanged().getTime() <= newDate.getTime()) {
            HassioState newState = globalQueue.poll();

            firstLayer.addCausalNode(new CausalNode(newState, null));
        }

        if(!firstLayer.isEmpty()) {
            causalStack.addLayer(firstLayer);

            if(this.isPredicting()) {
                causalStack = this.deduceTick(newDate, firstLayer, lastStates, simulatedRulesEnabled, snoozedActions, future);
                //runRequired = this.detectConflicts(newDate, lastStates, causalStack, firstLayer, snoozedActions, future);
            }

            this.commitPredictedStates(causalStack, future);
        }
    }

    /**
     * Filter the list of inconsistency conflicts given the full causal stack
     * @param causalStack
     * @param conflictsMapping
     */
    private void filterConflicts(CausalStack causalStack, HashMap<String,  List<Conflict>> conflictsMapping) {
        if(causalStack.isEmpty()) return;

        // System.out.println("Filtering conflicts on the causal stack");

        // Group all potential changes by entityID
        List<CausalNode> potentialChanges = causalStack.flatten();
        List<String> alreadyVisited = new ArrayList<>();
        List<String> conflictingChangesEntities = new ArrayList<>();

        for(int i = 0; i < potentialChanges.size(); ++i) {
            List<CausalNode> conflictingChanges = new ArrayList<>();
            String initialEntityID = potentialChanges.get(i).getState().entity_id;

            // Don't visit an entity multiple times
            if (!alreadyVisited.contains(initialEntityID)) {
                alreadyVisited.add(initialEntityID);

                for (int j = i; j < potentialChanges.size(); ++j) {
                    if (potentialChanges.get(j).getState().entity_id.equals(initialEntityID)) {
                        conflictingChanges.add(potentialChanges.get(j));
                    }
                }

                if (conflictingChanges.size() > 1) {
                    conflictingChangesEntities.add(initialEntityID);
                }
            }
        }

        List<Conflict> removeList = new ArrayList<>();
        for (Conflict conflict : conflictsMapping.get("INCONSISTENCY")) {
            if (!conflictingChangesEntities.contains(conflict.conflictingEntities.get(0)))
                removeList.add(conflict);
        }
        List<Conflict> filteredConflictList = conflictsMapping.get("INCONSISTENCY");
        filteredConflictList.removeAll(removeList);
        conflictsMapping.put("INCONSISTENCY", filteredConflictList);
    }

    /**
     * Detect conflicts and update conflict list
     * @param causalStack
     * @param conflictsMapping, this can be updated with new found conflicts
     * @param flags, [0] find Loops, [1] find Inconsistencies, [2] find Redundancies
     * @return true when a conflict is found
     */
    private boolean detectConflicts(CausalStack causalStack, HashMap<String,  List<Conflict>> conflictsMapping, HashMap<CausalNode, List<CausalNode>> causalityMapping, boolean[] flags) {
        if(causalStack.isEmpty()) return false;

        //System.out.println("Detecting conflicts on the causal stack");
        //causalStack.print();

        boolean conflictDetected = false;

        // Group all potential changes by entityID
        List<CausalNode> potentialChanges = causalStack.flatten();
        List<String> alreadyVisited = new ArrayList<>();

        for(int i = 0; i < potentialChanges.size(); ++i) {
            List<CausalNode> conflictingChanges = new ArrayList<>();
            String initialEntityID = potentialChanges.get(i).getState().entity_id;
            boolean newConflictFound = false;

            // Don't visit an entity multiple times
            if (!alreadyVisited.contains(initialEntityID)) {
                alreadyVisited.add(initialEntityID);

                for (int j = i; j < potentialChanges.size(); ++j) {
                    if (potentialChanges.get(j).getState().entity_id.equals(initialEntityID)) {
                        conflictingChanges.add(potentialChanges.get(j));
                    }
                }

                if (conflictingChanges.size() > 1) {
                    System.out.println("CONFLICT DETECTED FOR " + initialEntityID);
                    // Specify conflict as INCONSISTENCY, REDUNDANCY or LOOP
                    // Find LOOPS
                    List<CausalNode> loopingNodes = detectLoop(causalStack, conflictingChanges, causalityMapping);
                    if (!loopingNodes.isEmpty() && flags[0]) {
                        // LOOP found
                        resolveLoop(initialEntityID, loopingNodes, conflictsMapping);
                        newConflictFound = true;
                    } else {
                        if (flags[1]) {
                            // INCONSISTENCY found
                            newConflictFound = resolveInconsistency(initialEntityID, conflictingChanges, conflictsMapping);
                        }
                        if (flags[2]) {
                            // Find REDUNDANCIES
                            HashMap<CausalNode, List<CausalNode>> redundancyMapping = detectRedundancy(conflictingChanges);
                            if (!redundancyMapping.isEmpty()) {
                                // REDUNDANCIES found
                                newConflictFound = resolveRedundancy(initialEntityID, redundancyMapping, conflictsMapping);
                            }
                        }
                    }
                }

                if (!conflictDetected && newConflictFound) {
                    conflictDetected = true;
                }
            }
        }

        return conflictDetected;
    }

    /**
     * Retrieves all the nodes that are involved in the loop if one should exist
     * @param causalStack
     * @param conflictingChanges
     * @param causalityMapping
     * @return empty list if none is found
     */
    private List<CausalNode> detectLoop(CausalStack causalStack, List<CausalNode> conflictingChanges, HashMap<CausalNode, List<CausalNode>> causalityMapping) {
        System.out.println("Detecting loops");
        List<CausalNode> result = new ArrayList<>();

        CausalNode startingNode = causalStack.getHighestNode(conflictingChanges);
        if (startingNode == null)
            return result;
        else
            result.add(startingNode);

        return recursiveLoopDetection(startingNode, conflictingChanges, causalityMapping, result);
    }

    /**
     * Recursively detect loops
     * @param startNode
     * @param conflictingChanges
     * @param causalityMapping
     * @param result
     * @return empty list if none is found
     */
    private List<CausalNode> recursiveLoopDetection(CausalNode startNode, List<CausalNode> conflictingChanges, HashMap<CausalNode, List<CausalNode>> causalityMapping, List<CausalNode> result) {
        List<CausalNode> mapping = causalityMapping.get(startNode);
        if (mapping == null) {
            return new ArrayList<>();
        }
        for (CausalNode node : mapping) {
            List<CausalNode> newResult = new ArrayList<>(result);
            newResult.add(node);
            // Check if this is the conflict
            if (conflictingChanges.contains(node) && !node.equals(result.get(0)) && node.getState().isSimilar(result.get(0).getState())) {
                System.out.println("LOOP FOUND FOR: " + node.getState().entity_id);
                return newResult;
            } else {
                List<CausalNode> recursiveResult = recursiveLoopDetection(node, conflictingChanges, causalityMapping, newResult);
                if (!recursiveResult.isEmpty()) {
                    return recursiveResult;
                }
            }
        }
        return new ArrayList<>();
    }

    private void resolveLoop(String initialEntityID, List<CausalNode> loopingNodes, HashMap<String,  List<Conflict>> conflictsMapping) {
        System.out.println("LOOP DETECTED FOR " + initialEntityID);
        // LOOP found, add conflict and create solution immediately
        Conflict conflict = new Conflict(Arrays.asList(initialEntityID), loopingNodes);
        List<Conflict> loopingConflicts = conflictsMapping.get("LOOP");
        loopingConflicts.add(conflict);
        conflictsMapping.put("LOOP", loopingConflicts);
        solveLoopConflict(conflict);
    }

    private void solveLoopConflict(Conflict conflict) {
        // Snooze last in loop
        List<ConflictingAction> snoozedActions = new ArrayList<>();
        snoozedActions.add(conflict.getConflictingActions().get(conflict.getConflictingActions().size() - 1));
        // Actively select the first in loop
        List<ConflictingAction> activeActions = new ArrayList<>();
        activeActions.addAll(conflict.getConflictingActions().subList(0, conflict.getConflictingActions().size()-1));

        ConflictSolution solution = new ConflictSolution(conflict.conflictingEntities.get(0));
        solution.setConflictingActions(conflict.getConflictingActions());
        solution.setSnoozedActions(snoozedActions);
        solution.setActiveActions(activeActions);
        solutionManager.addSolution(solution);
    }

    private HashMap<CausalNode, List<CausalNode>> detectRedundancy(List<CausalNode> conflictingChanges) {
        HashMap<CausalNode, List<CausalNode>> redundancyMapping = new HashMap<>();
        for (int i = 0; i < conflictingChanges.size(); ++i) {
            CausalNode comparingNode = conflictingChanges.get(i);
            List<CausalNode> redundancies = new ArrayList<>();
            for (int j = i+1; j < conflictingChanges.size(); ++j) {
                CausalNode node = conflictingChanges.get(j);
                if (!node.equals(comparingNode) && node.getState().isSimilar(comparingNode.getState())) {
                    if (!redundancies.contains(node)) {
                        redundancies.add(node);
                    }
                }
            }
            if (!redundancies.isEmpty()) {
                redundancyMapping.put(comparingNode, redundancies);
            }
        }
        return redundancyMapping;
    }

    private boolean resolveRedundancy(String initialEntityID, HashMap<CausalNode, List<CausalNode>> redundancyMapping, HashMap<String,  List<Conflict>> conflictsMapping) {
        boolean newConflictFound = false;
        System.out.println("REDUNDANCY DETECTED FOR " + initialEntityID);
        // REDUNDANCIES found, for every redundancy: find existing conflict, add conflict and create solution immediately
        for (CausalNode key : redundancyMapping.keySet()) {
            List<CausalNode> redundancyList = new ArrayList<>();
            redundancyList.add(key);
            redundancyList.addAll(redundancyMapping.get(key));
            boolean temp = false;

            // Because redundancies are not solved immediately, there are existing redundancy conflicts
            // Find existing conflict
            Conflict existingRedundancy = null;
            for (Conflict redundancyConflict :  conflictsMapping.get("REDUNDANCY")) {
                if (redundancyConflict.containsSameActions(redundancyList)) {
                    existingRedundancy = redundancyConflict;
                    break;
                }
            }
            // Add conflict
            if (existingRedundancy != null) {
                temp = existingRedundancy.updateConflict(redundancyList);
            } else {
                Conflict newRedundancy = new Conflict(Arrays.asList(initialEntityID), redundancyList);
                List<Conflict> redundancyConflicts = conflictsMapping.get("REDUNDANCY");
                redundancyConflicts.add(newRedundancy);
                conflictsMapping.put("REDUNDANCY", redundancyConflicts);
                newConflictFound = true;
            }

            if (temp) {
                newConflictFound = true;
            }
        }
        return newConflictFound;
    }

    private void solveRedundancyConflict(Conflict conflict) {
        List<ConflictingAction> activeActions = new ArrayList<>();
        activeActions.add(conflict.getConflictingActions().get(0));
        List<ConflictingAction> snoozedActions = conflict.getConflictingActions().subList(1, conflict.getConflictingActions().size());

        ConflictSolution solution = new ConflictSolution(conflict.conflictingEntities.get(0));
        solution.setConflictingActions(conflict.getConflictingActions());
        solution.setSnoozedActions(snoozedActions);
        solution.setActiveActions(activeActions);
        solutionManager.addSolution(solution);
    }

    /**
     * Resolve inconsistency conflicts
     * @param initialEntityID
     * @param conflictingChanges
     * @param conflictsMapping
     * @return true when new conflict is resolved
     */
    private boolean resolveInconsistency(String initialEntityID, List<CausalNode> conflictingChanges, HashMap<String,  List<Conflict>> conflictsMapping) {
        boolean newConflictFound = false;
        System.out.println("INCONSISTENCY DETECTED FOR " + initialEntityID);
        // INCONSISTENCY found, find existing conflict and add conflict
        // Find existing conflict
        Conflict existingInconsistency = null;
        for (Conflict inconsistencyConflict :  conflictsMapping.get("INCONSISTENCY")) {
            if (inconsistencyConflict.conflictingEntities.contains(initialEntityID)) {
                existingInconsistency = inconsistencyConflict;
                break;
            }
        }
        // Add conflict
        if (existingInconsistency != null) {
            newConflictFound = existingInconsistency.updateConflict(conflictingChanges);
        } else {
            List<Conflict> inconsistencyConflicts = conflictsMapping.get("INCONSISTENCY");
            inconsistencyConflicts.add(new Conflict(Arrays.asList(initialEntityID), conflictingChanges));
            conflictsMapping.put("INCONSISTENCY", inconsistencyConflicts);
            newConflictFound = true;
        }

        return newConflictFound;
    }

    /**
     * Apply a solution to the firstLayer
     * @param newDate
     * @param potentialSolution
     * @param lastStates
     * @param firstLayer
     * @param snoozedActions
     * @return
     */
    private SolutionExecutionEvent applySolution(Date newDate, ConflictSolution potentialSolution, HashMap<String, HassioState> lastStates, CausalLayer firstLayer, List<ConflictingAction> snoozedActions) {
        SolutionExecutionEvent solutionExecution = new SolutionExecutionEvent(potentialSolution.solutionID, newDate);

        // Snooze actions, if any
        snoozedActions.addAll(potentialSolution.snoozedActions);

        // simulate custom actions, if any
        for(Action customAction : potentialSolution.customActions) {
            List<HassioState> solutionStates = customAction.simulate(newDate, lastStates);
            solutionExecution.addActionExecuted(customAction.id, solutionStates);

            for(HassioState solutionState : solutionStates) {
                firstLayer.addCausalNode(new CausalNode(solutionState, solutionExecution));
            }
        }

        return solutionExecution;
    }

    /**
     * Find solutions and apply them if possible
     * @param newDate
     * @param conflictsMapping
     * @param lastStates
     * @param firstLayer
     * @param snoozedActions
     * @param future
     */
    private void applySolution(Date newDate, HashMap<String, List<Conflict>> conflictsMapping, HashMap<String, HassioState> lastStates, CausalLayer firstLayer, List<ConflictingAction> snoozedActions, Future future, boolean[] flags) {
        List<Conflict> removeList = new ArrayList<>();
        List<Conflict> conflicts = new ArrayList<>();
        for (List<Conflict> values : conflictsMapping.values())
            conflicts.addAll(values);

        // Find solution for conflicts and apply them
        for (Conflict conflict : conflicts) {
            ConflictSolution potentialSolution = null;
            if ((flags[0] && conflict.isLoop()) || (flags[2] && conflict.isRedundancy()) || flags[1]) {
                potentialSolution = solutionManager.getSolutionForConflict(conflict);
            }

            if (potentialSolution != null) {
                SolutionExecutionEvent solutionExecution = this.applySolution(newDate, potentialSolution, lastStates, firstLayer, snoozedActions);
                //future.addExecutionEvent(solutionExecution); This is not necessary. CommitPredictedStates already adds all the states and executions to the future
                if (!future.getFutureConflictSolutions().contains(potentialSolution))
                    future.addFutureConflictSolution(potentialSolution);
                System.out.println("Solution applied, rerun required");
                removeList.add(conflict);
                // TODO: A solution should only be applied once?
            }
        }

        // Remove solved conflicts
        for (String key : conflictsMapping.keySet()) {
            List<Conflict> conflictList = conflictsMapping.get(key);
            conflictList.removeAll(removeList);
            conflictsMapping.put(key, conflictList);
        }
    }

    /**
     * Commit the predicted states on the causal stack to the future
     * @param causalStack
     * @param future
     */
    private void commitPredictedStates(CausalStack causalStack, Future future) {
        for(CausalLayer causalLayer : causalStack.getLayers()) {
            future.addCausalLayer(causalLayer);
        }

        List<CausalNode> finalNewChanges = causalStack.flatten();

        for(CausalNode node : finalNewChanges) {
         //   future.addFutureState(node.getState());

            // If an executionEvent exists and is not already added to the future, add it to the future
            if(node.getExecutionEvent() != null && !future.getFutureExecutions().contains(node.getExecutionEvent())) {
                future.addExecutionEvent(node.getExecutionEvent());
            }
        }
    }

    /**
     *
     * @param newDate
     * @param firstLayer
     * @param lastStates
     * @param simulatedRulesEnabled
     * @param snoozedActions
     * @return
     */
    private CausalStack deduceTick(Date newDate, CausalLayer firstLayer, HashMap<String, HassioState> lastStates, HashMap<String, Boolean> simulatedRulesEnabled, List<ConflictingAction> snoozedActions, Future future) {
        CausalStack causalStack = null;

        HashMap<CausalNode, List<CausalNode>> causalityMapping = new HashMap<>();
        boolean runRequired = true;
        boolean lastRun = true;
        boolean[] flags = {true, true, false};

        HashMap<String, List<Conflict>> conflictsMapping = new HashMap<>();
        conflictsMapping.put("INCONSISTENCY", new ArrayList<>());
        conflictsMapping.put("REDUNDANCY", new ArrayList<>());
        conflictsMapping.put("LOOP", new ArrayList<>());
        List<Conflict> remainingConflicts = new ArrayList<>();

        while(runRequired) {
            causalStack = new CausalStack();
            causalStack.addLayer(firstLayer);
            CausalLayer newLayer = firstLayer;
            causalityMapping.clear();
            runRequired = false;

            // Determine future (could contain inconsistencies and loops)
            while (!newLayer.isEmpty() && !runRequired) {
                List<Conflict> conflicts = new ArrayList<>();
                for (List<Conflict> values : conflictsMapping.values())
                    conflicts.addAll(values);

                newLayer = deduceLayer(newDate, causalStack, lastStates, simulatedRulesEnabled, snoozedActions, conflicts, causalityMapping);

                if (!newLayer.isEmpty()) {
                    causalStack.addLayer(newLayer);

                    // Detect conflicts (inconsistencies and loops)
                    runRequired = detectConflicts(causalStack, conflictsMapping, causalityMapping, flags);

                    List<Conflict> otherConflicts = this.conflictVerificationManager.verifyConflicts(newDate, lastStates, causalStack);

                    if(!otherConflicts.isEmpty()) {
                        System.out.println("Extra conflicts found: " + otherConflicts.size());
                        remainingConflicts.addAll(otherConflicts);
                    }

                    // If conflicts are found, find solution and apply it. Rerun everything!
                    if (runRequired) {
                        applySolution(newDate, conflictsMapping, lastStates, firstLayer, snoozedActions, future, flags);
                    }
                } else if (lastRun){
                    // Resolve all redundancy conflicts, apply solution and rerun
                    flags = new boolean[]{false, false, true};
                    // Detect conflicts (redundancies)
                    runRequired = detectConflicts(causalStack, conflictsMapping, causalityMapping, flags);
                    // If conflicts are found, find solution and apply it. Rerun everything!
                    if (runRequired) {
                        for (Conflict conflict : conflictsMapping.get("REDUNDANCY")) {
                            solveRedundancyConflict(conflict);
                        }
                        applySolution(newDate, conflictsMapping, lastStates, firstLayer, snoozedActions, future, flags);
                    }

                    // Filter all conflicts, making sure that only true conflicts remain
                    filterConflicts(causalStack, conflictsMapping);

                    lastRun = false;
                }
            }
        }

        // Add all conflicts to the future
        for (List<Conflict> values : conflictsMapping.values()) {
            remainingConflicts.addAll(values);
        }

        future.addFutureConflicts(remainingConflicts);

        return causalStack;
    }

    /**
     * Determine which new states would occur (in a new layer) based on the new states from the previous layer)
     * @param newDate
     * @param causalStack
     * @param lastStates
     * @param simulatedRulesEnabled
     * @param snoozedActions the actions which are snoozed (e.g. by conflict solutions)
     * @param conflicts
     * @param causalityMapping
     * @return
     */
    private CausalLayer deduceLayer(Date newDate, CausalStack causalStack, HashMap<String, HassioState> lastStates, HashMap<String, Boolean> simulatedRulesEnabled, List<ConflictingAction> snoozedActions, List<Conflict> conflicts, HashMap<CausalNode, List<CausalNode>> causalityMapping) {
        // Build the states for this layer
        HashMap<String, HassioState> layerSpecificStates = buildLayerSpecificStates(lastStates, causalStack, conflicts);

        CausalLayer previousLayer = causalStack.getTopLayer();
        CausalLayer newLayer = new CausalLayer(newDate);

        // Build a list of changes in this layer
        List<HassioChange> newChanges = new ArrayList<>();
        for(int i = 0; i < previousLayer.getNumStates(); ++i) {
            CausalNode node = previousLayer.getCausalNode(i);
            causalityMapping.put(node, new ArrayList<>());

            HassioState newState = node.getState();
            HassioState lastState = lastStates.get(newState.entity_id);

            // Take only nodes into account that are not part of a conflict
            if (!isConflictingNode(node, conflicts)) {
                newChanges.add(new HassioChange(newState.entity_id, lastState, newState, newState.getLastChanged()));
            }
        }

        // Pass the stateChange to the set of rules and to the implicit behavior
        newLayer.addAll(this.verifyExplicitRules(newDate, newChanges, layerSpecificStates, simulatedRulesEnabled, snoozedActions, causalityMapping));
        newLayer.addAll(this.verifyImplicitRules(newDate, layerSpecificStates));

        return newLayer;
    }

    /**
     * Pass the changes to all rules developed by the user
     * @param date
     * @param newChanges
     * @param states
     * @param simulatedRulesEnabled
     * @param snoozedActions the actions which are snoozed (e.g. by conflict solutions)
     * @return
     */
    private List<CausalNode> verifyExplicitRules(Date date, List<HassioChange> newChanges, HashMap<String, HassioState> states, HashMap<String, Boolean> simulatedRulesEnabled, List<ConflictingAction> snoozedActions, HashMap<CausalNode, List<CausalNode>> causalityMapping) {
        List<CausalNode> result = new ArrayList<>();

        List<RuleExecutionEvent> triggerEvents = this.rulesManager.verifyTriggers(date, newChanges, simulatedRulesEnabled);
        List<RuleExecutionEvent> conditionTrueEvents = this.rulesManager.verifyConditions(states, triggerEvents);

        for(RuleExecutionEvent potentialExecutionEvent : conditionTrueEvents) {
            // Find which CausalNodes caused this potentialExecution
            List<CausalNode> causalityNodes = new ArrayList<>();
            for (CausalNode node : causalityMapping.keySet()) {
                if(potentialExecutionEvent.isInConditionSatisfied(node.getState().context)) {
                    causalityNodes.add(node);
                }
            }
            // Find which actions should be snoozed for this rule
            List<String> ruleSpecificSnoozedActions = new ArrayList<>();
            for(ConflictingAction conflictingAction : snoozedActions) {
                if(conflictingAction.rule_id.equals(potentialExecutionEvent.getTrigger().id)) ruleSpecificSnoozedActions.add(conflictingAction.action_id);
            }

            HashMap<String, List<HassioState>> proposedActionState = potentialExecutionEvent.getTrigger().simulate(potentialExecutionEvent, states, ruleSpecificSnoozedActions);

            // For every proposed action, add it to the queue
            List<CausalNode> newNodes = new ArrayList<>();
            for(String actionID : proposedActionState.keySet()) {
                for(HassioState proposedState : proposedActionState.get(actionID)) {
                    newNodes.add(new CausalNode(proposedState, potentialExecutionEvent));
                }
            }
            result.addAll(newNodes);
            // For every causalityNode, add new nodes to their mapping
            for (CausalNode node : causalityNodes) {
                List<CausalNode> mapping = causalityMapping.get(node);
                mapping.addAll(newNodes);
                causalityMapping.put(node, mapping);
            }

            // Add the context of the simulated actions as a result in the potentialTriggerEvent
            for(String actionID : proposedActionState.keySet()) {
                potentialExecutionEvent.addActionExecuted(actionID, proposedActionState.get(actionID));
            }
        }

        return result;
    }

    /**
     * Give devices a chance to updates themselves (e.g. update temperature)
     * @param states
     * @return
     */
    private List<CausalNode> verifyImplicitStates(Date newDate, HashMap<String, HassioState> states) {
        List<CausalNode> result = new ArrayList<>();

        List<ImplicitBehaviorEvent> behaviorEvents = hassioDeviceManager.predictImplicitStates(newDate, states);

        for(ImplicitBehaviorEvent behaviorEvent : behaviorEvents) {
            behaviorEvent.setTrigger(this.rulesManager.getRuleById(this.rulesManager.RULE_IMPLICIT_BEHAVIOR));

            for(HassioState newActionState : behaviorEvent.getActionStates()) {
                result.add(new CausalNode(newActionState, behaviorEvent));
            }
        }

        return result;
    }

    /**
     * Pass the stateChange to the implicit rules (e.g. turn heater on/off)
     * @param states
     * @return
     */
    private List<CausalNode> verifyImplicitRules(Date newDate, HashMap<String, HassioState> states) {
        List<CausalNode> result = new ArrayList<>();

        List<ImplicitBehaviorEvent> behaviorEvents = hassioDeviceManager.predictImplicitRules(newDate, states);

        for(ImplicitBehaviorEvent behaviorEvent : behaviorEvents) {
            behaviorEvent.setTrigger(this.rulesManager.getRuleById(this.rulesManager.RULE_IMPLICIT_BEHAVIOR));

            for(HassioState newActionState : behaviorEvent.getActionStates()) {
                result.add(new CausalNode(newActionState, behaviorEvent));
            }
        }

        return result;
    }

    /**
     * Build a hashmap with the last states, updated with all changes that are specific to this branch in the three
     * @param lastStates
     * @return
     */
    private HashMap<String, HassioState> buildLayerSpecificStates(HashMap<String, HassioState> lastStates, CausalStack causalStack, List<Conflict> conflicts) {
        HashMap<String, HassioState> branchSpecificStates = new HashMap<>();

        for(String deviceID : lastStates.keySet()) {
            branchSpecificStates.put(deviceID, lastStates.get(deviceID));
        }

        for(int i = 0; i < causalStack.getNumLayers(); ++i) {
            for(CausalNode causalNode : causalStack.getLayer(i).getCausalNodes()) {
                if (!isConflictingNode(causalNode, conflicts)) {
                    branchSpecificStates.put(causalNode.getState().entity_id, causalNode.getState());
                }
            }
        }

        return branchSpecificStates;
    }

    /**
     * Determines if node is part of a conflict
     * @param node
     * @param conflicts
     * @return true when node is part of a conflict
     */
    private boolean isConflictingNode(CausalNode node, List<Conflict> conflicts) {
        for (Conflict conflict : conflicts) {
            if (conflict.conflictingEntities.contains(node.getState().entity_id) || conflict.conflictingEntities.contains("Loop_"+node.getState().entity_id)) {
                return true;
            }
        }
        return false;
    }

    public void stopFuturePredictions() {
        this.predicting = false;
        this.updateFuturePredictions(); // Generate a new future without simulating rules
    }

    public void startFuturePredictions() {
        this.predicting = true;
        this.updateFuturePredictions();
    }

    public boolean isPredicting() {
        return this.predicting;
    }

    public long getTickRate() {
        return tickRate;
    }

    public void setTickRate(long tickRate) {
        this.tickRate = tickRate;
        this.updateFuturePredictions();
    }

    public long getPredictionWindow() {
        return predictionWindow;
    }

    public void setPredictionWindow(long predictionWindow) {
        this.predictionWindow = predictionWindow;
        this.updateFuturePredictions();
    }
}