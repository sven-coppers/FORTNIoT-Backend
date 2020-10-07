package sven.phd.iot.predictions;

import sven.phd.iot.ContextManager;
import sven.phd.iot.api.request.SimulationRequest;
import sven.phd.iot.api.resources.StateResource;
import sven.phd.iot.conflicts.Conflict;
import sven.phd.iot.conflicts.ConflictVerificationManager;
import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.overrides.SnoozedAction;
import sven.phd.iot.rules.*;

import java.util.*;

public class PredictionEngine {
    private final ContextManager contextManager;
    private final RulesManager rulesManager;
    private final HassioDeviceManager deviceManager;
    private final ConflictVerificationManager conflictVerificationManager;
    private Future future;
    private Boolean predicting;
    private long tickRate = 5; // minutes
    private long predictionWindow = 1 * 24 * 60; // 1 day in minutes
    private Date lastLoggedDate = null;

    public PredictionEngine(ContextManager contextManager) {
        this.contextManager = contextManager;
        this.rulesManager = contextManager.getRulesManager();
        this.deviceManager = contextManager.getHassioDeviceManager();
        this.conflictVerificationManager = contextManager.getConflictVerificationManager();
        this.future = new Future(new HashMap<>());
        this.predicting = false;
    }

    public Future getFuture() {
        return future;
    }

    /**
     * Predict the future with the latest information we have
     */
    public void updateFuturePredictions() {
        this.future = predictFuture(contextManager.gatherPredictionInput());
        StateResource.getInstance().broadcastRefresh("Predictions updated");
    }

    /**
     * Predict an alternative future with simulated input
     */
    public Future whatIf(SimulationRequest simulationRequest) {
        PredictionInput predictionInput = contextManager.gatherPredictionInput();
        predictionInput.mergeSimulationRequest(simulationRequest);

        return predictFuture(predictionInput);
    }

    /**
     * Predict the future states and event of each HassioDevice and each rule
     *
     * @post: Each HassioDevice and Each Rule will have a cached version of the outcome
     */
    private Future predictFuture(PredictionInput predictionInput) {
        // Initialise the queue with changes we already know
        PriorityQueue<HassioState> queue = new PriorityQueue<>();

        Future future = new Future(deviceManager.getCurrentStates());
        queue.addAll(deviceManager.predictFutureStates());
        queue.addAll(predictionInput.getHassioStates());

        Date lastFrameDate = new Date(); // Prediction start
        Date predictionEnd = new Date(new Date().getTime() + getPredictionWindow() * 60L * 1000L); // Convert prediction window from minutes to milliseconds

        // Predict the first day with high precision
        while (lastFrameDate.getTime() < predictionEnd.getTime()) {
            Date nextTickDate = new Date(lastFrameDate.getTime() + (getTickRate() * 60L * 1000L)); // Convert tickRate from minutes to milliseconds

            // If there is an element in the queue that will happen before the tick
            if (!queue.isEmpty() && queue.peek().getLastChanged().getTime() < nextTickDate.getTime()) {
                nextTickDate = queue.peek().getLastChanged();
            }

            lastFrameDate = this.tick(nextTickDate, queue, future, predictionInput);

            // TODO: When a solution is applied, and the future is reverted, lastFrameDate could be later
            // TODO: The queue should be restored as well
        }

        // Finish predicting the rest of the queue (within the prediction window)
        while (!queue.isEmpty()) {
            if (queue.peek().getLastChanged().getTime() < predictionEnd.getTime()) {
                this.tick(queue.peek().getLastChanged(), queue, future, predictionInput);
            } else {
                queue.poll();
            }
        }

        int oldNumConflicts = future.getFutureConflicts().size();

        future.simplifyConflicts();

        System.out.println("Finished deducing " + future.getNumDeducedPredictions() + " predictions from " + future.getNumSelfSustainingPredictions() + " self-sustaining predictions. Reduced " + oldNumConflicts + " to " + future.getFutureConflicts().size() + " conflicts.");

        future.simplifyConflicts();

        return future;
    }

    /**
     * Simulate a tick, and resolve conflicts if needed
     *
     * @param newDate
     * @param globalQueue
     * @param future
     * @param predictionInput
     * @return the most recent completed tick (when a solution is applied and something needs to be reverted, the last untouched date should be returned)
     */
    private Date tick(Date newDate, PriorityQueue<HassioState> globalQueue, Future future, PredictionInput predictionInput) {

        List<HassioState> firstLayer = new ArrayList<>(); // Never changes, because no rules are executed yet

        // IMPLICIT Let the devices predict their state, only once a tick, based on the past states (e.g. temperature)
        if (this.isPredicting()) {
            firstLayer.addAll(this.deviceManager.predictTickFutureStates(newDate, future));
        }

        // BASELINE: Add states from the global queue (before the current date), which could induce conflicts
        while (!globalQueue.isEmpty() && globalQueue.peek().getLastChanged().getTime() <= newDate.getTime()) {
            HassioState newState = globalQueue.poll();

            firstLayer.add(newState);
        }

        this.deduceTick(newDate, firstLayer, future, predictionInput);

        return newDate; // TODO: Revert if needed
    }

    /**
     * Deduce everything that happens inside a tick() and add it to the future
     *
     * @param newDate
     * @param lastLayer
     * @return
     */
    private void deduceTick(Date newDate, List<HassioState> lastLayer, Future future, PredictionInput predictionInput) {
        // Determine future (could contain inconsistencies and loops)
        while (!lastLayer.isEmpty()) {
            List<Conflict> conflicts = this.submitStatesToFuture(newDate, future, lastLayer);

            if (!conflicts.isEmpty()) {
                future.addFutureConflicts(conflicts);
                // TODO: Try to solve them

                // If conflicts are found, find solution and apply it. Rerun everything!
                //    if (runRequired) { -> Revert to a specific moment in time, set the mainloop to that moment in time
                // TODO: What was in the queue at that time? -> OR restart everything???
                // TODO:           applySolution(newDate, conflictsMapping, lastStates, firstLayer, snoozedActions, future, flags);
                // TODO: solveRedundancyConflict(conflict);
            }

            if(this.isPredicting()) {
                lastLayer = deduceLayer(newDate, lastLayer, future, predictionInput);
            } else {
                lastLayer.clear();
            }
        }
        // TODO: Solving a solution means deleting it from the future. (because you backtrack to the beginning of the conflict)
    }

    /**
     * Determine which new states would occur (in a new layer) based on the new states from the previous layer)
     * @param newDate
     * @return
     */
    private List<HassioState> deduceLayer(Date newDate, List<HassioState> previousLayer, Future future, PredictionInput predictionInput) {
        // Build the states for this layer
        List<HassioState> newLayer = new ArrayList<>();

        // Build a list of changes in this layer
        List<HassioChange> newChanges = new ArrayList<>();
        for(int i = 0; i < previousLayer.size(); ++i) {
            HassioState state = previousLayer.get(i);

            HassioState newState = state;
            HassioState lastState = future.getSecondLastState(state.entity_id);

            newChanges.add(new HassioChange(newState.entity_id, lastState, newState, newState.getLastChanged()));
        }

        // Pass the stateChange to the set of rules and to the implicit behavior
        newLayer.addAll(this.verifyExplicitRules(newDate, newChanges, future, predictionInput));
        newLayer.addAll(deviceManager.predictLayerFutureStates(newDate, future));

        return newLayer;
    }

    /**
     * Pass the changes to all rules developed by the user
     * @param date
     * @param newChanges
     * @param future

     * @return
     */
    private List<HassioState> verifyExplicitRules(Date date, List<HassioChange> newChanges, Future future, PredictionInput predictionInput) {
        List<HassioState> result = new ArrayList<>();

        HashMap<String, HassioState> states = future.getLastStates();

        List<RuleExecution> triggerEvents = this.rulesManager.verifyTriggers(date, states, newChanges, predictionInput.getEnabledRules());
        List<RuleExecution> conditionTrueEvents = this.rulesManager.verifyConditions(states, triggerEvents);

        for(RuleExecution ruleExecution : conditionTrueEvents) {
            // TODO: Find which actions should be snoozed for this rule
            String triggerEntityID = ruleExecution.triggerEntity;
            Trigger rule = rulesManager.getRule(ruleExecution.ruleID);
            HashMap<String, Action> ruleActions = rule.getActions();

            for(String potentialActionID : ruleActions.keySet()) {
                Action action = ruleActions.get(potentialActionID);

                // CHECK IF THE ACTION IS SNOOZED OR NOT
                SnoozedAction snoozedAction = predictionInput.isSnoozed(potentialActionID, triggerEntityID, ruleExecution.datetime);

                if(snoozedAction == null) {
                    List<HassioState> resultingStates = action.simulate(ruleExecution.datetime, states);
                    ruleExecution.addActionExecution(new ActionExecution(date, potentialActionID, resultingStates));
                    result.addAll(resultingStates);
                } else {
                    // Also add action Executions for actions that were snoozed, so they can be re enabled later
                    ruleExecution.addActionExecution(new ActionExecution(date, potentialActionID, snoozedAction.snoozedActionID));
                }
            }

            future.addExecutionEvent(ruleExecution);
        }

        return result;
    }

    /**
     * Filter the list of inconsistency conflicts given the full causal stack
     * @param causalStack
     * @param conflictsMapping
     */
   /*  private void filterConflicts(CausalStack causalStack, HashMap<String,  List<Conflict>> conflictsMapping) {
       if(causalStack.isEmpty()) return;

        // System.out.println("Filtering conflicts on the causal stack");

        // Group all potential changes by entityID
        List<HassioState> potentialChanges = causalStack.flatten();
        List<String> alreadyVisited = new ArrayList<>();
        List<String> conflictingChangesEntities = new ArrayList<>();

        for(int i = 0; i < potentialChanges.size(); ++i) {
            List<HassioState> conflictingChanges = new ArrayList<>();
            String initialEntityID = potentialChanges.get(i).entity_id;

            // Don't visit an entity multiple times
            if (!alreadyVisited.contains(initialEntityID)) {
                alreadyVisited.add(initialEntityID);

                for (int j = i; j < potentialChanges.size(); ++j) {
                    if (potentialChanges.get(j).entity_id.equals(initialEntityID)) {
                        conflictingChanges.add(potentialChanges.get(j));
                    }
                }

                if (conflictingChanges.size() > 1) {
                    conflictingChangesEntities.add(initialEntityID);
                }
            }
        }

        List<Conflict> removeList = new ArrayList<>();

        filteredConflictList.removeAll(removeList);
        conflictsMapping.put("INCONSISTENCY", filteredConflictList);
    }*/

    /**
     * Detect conflicts and update conflict list
     * @param causalStack
     * @param conflictsMapping, this can be updated with new found conflicts
     * @param flags, [0] find Loops, [1] find Inconsistencies, [2] find Redundancies
     * @return true when a conflict is found
     */
   /* private boolean detectConflicts(CausalStack causalStack, HashMap<String,  List<Conflict>> conflictsMapping, HashMap<HassioState, List<HassioState>> causalityMapping, boolean[] flags) {
        if(causalStack.isEmpty()) return false;

        //System.out.println("Detecting conflicts on the causal stack");
        //causalStack.print();

        boolean conflictDetected = false;

        // Group all potential changes by entityID
        List<HassioState> potentialChanges = causalStack.flatten();
        List<String> alreadyVisited = new ArrayList<>();

        for(int i = 0; i < potentialChanges.size(); ++i) {
            List<HassioState> conflictingChanges = new ArrayList<>();
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
                    List<HassioState> loopingstates = detectLoop(causalStack, conflictingChanges, causalityMapping);
                    if (!loopingstates.isEmpty() && flags[0]) {
                        // LOOP found
                        resolveLoop(initialEntityID, loopingstates, conflictsMapping);
                        newConflictFound = true;
                    } else {
                        if (flags[1]) {
                            // INCONSISTENCY found
                            newConflictFound = resolveInconsistency(initialEntityID, conflictingChanges, conflictsMapping);
                        }
                        if (flags[2]) {
                            // Find REDUNDANCIES
                            HashMap<HassioState, List<HassioState>> redundancyMapping = detectRedundancy(conflictingChanges);
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
    } */

    /**
     * Retrieves all the states that are involved in the loop if one should exist
     * @param causalStack
     * @param conflictingChanges
     * @param causalityMapping
     * @return empty list if none is found
     */
  /*  private List<HassioState> detectLoop(CausalStack causalStack, List<HassioState> conflictingChanges, HashMap<HassioState, List<HassioState>> causalityMapping) {
        System.out.println("Detecting loops");
        List<HassioState> result = new ArrayList<>();

        HassioState startingstate = causalStack.getHigheststate(conflictingChanges);
        if (startingstate == null)
            return result;
        else
            result.add(startingstate);

        return recursiveLoopDetection(startingstate, conflictingChanges, causalityMapping, result);
    } */

    /**
     * Recursively detect loops
     * @param startstate
     * @param conflictingChanges
     * @param causalityMapping
     * @param result
     * @return empty list if none is found
     */
   /* private List<HassioState> recursiveLoopDetection(HassioState startstate, List<HassioState> conflictingChanges, HashMap<HassioState, List<HassioState>> causalityMapping, List<HassioState> result) {
        List<HassioState> mapping = causalityMapping.get(startstate);
        if (mapping == null) {
            return new ArrayList<>();
        }
        for (HassioState state : mapping) {
            List<HassioState> newResult = new ArrayList<>(result);
            newResult.add(state);
            // Check if this is the conflict
            if (conflictingChanges.contains(state) && !state.equals(result.get(0)) && state.getState().isSimilar(result.get(0).getState())) {
                System.out.println("LOOP FOUND FOR: " + state.getState().entity_id);
                return newResult;
            } else {
                List<HassioState> recursiveResult = recursiveLoopDetection(state, conflictingChanges, causalityMapping, newResult);
                if (!recursiveResult.isEmpty()) {
                    return recursiveResult;
                }
            }
        }
        return new ArrayList<>();
    }

    private void resolveLoop(String initialEntityID, List<HassioState> loopingstates, HashMap<String,  List<Conflict>> conflictsMapping) {
        System.out.println("LOOP DETECTED FOR " + initialEntityID);
        // LOOP found, add conflict and create solution immediately
        Conflict conflict = new Conflict(Arrays.asList(initialEntityID), loopingstates);
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

    private HashMap<HassioState, List<HassioState>> detectRedundancy(List<HassioState> conflictingChanges) {
        HashMap<HassioState, List<HassioState>> redundancyMapping = new HashMap<>();
        for (int i = 0; i < conflictingChanges.size(); ++i) {
            HassioState comparingstate = conflictingChanges.get(i);
            List<HassioState> redundancies = new ArrayList<>();
            for (int j = i+1; j < conflictingChanges.size(); ++j) {
                HassioState state = conflictingChanges.get(j);
                if (!state.equals(comparingstate) && state.getState().isSimilar(comparingstate.getState())) {
                    if (!redundancies.contains(state)) {
                        redundancies.add(state);
                    }
                }
            }
            if (!redundancies.isEmpty()) {
                redundancyMapping.put(comparingstate, redundancies);
            }
        }
        return redundancyMapping;
    }

    private boolean resolveRedundancy(String initialEntityID, HashMap<HassioState, List<HassioState>> redundancyMapping, HashMap<String,  List<Conflict>> conflictsMapping) {
        boolean newConflictFound = false;
        System.out.println("REDUNDANCY DETECTED FOR " + initialEntityID);
        // REDUNDANCIES found, for every redundancy: find existing conflict, add conflict and create solution immediately
        for (HassioState key : redundancyMapping.keySet()) {
            List<HassioState> redundancyList = new ArrayList<>();
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
    } */

    /**
     * Resolve inconsistency conflicts
     * @param initialEntityID
     * @param conflictingChanges
     * @param conflictsMapping
     * @return true when new conflict is resolved
     */
  /*  private boolean resolveInconsistency(String initialEntityID, List<HassioState> conflictingChanges, HashMap<String,  List<Conflict>> conflictsMapping) {
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
    } */

    /**
     * Apply a solution to the firstLayer
     * @param newDate
     * @param potentialSolution
     * @param lastStates
     * @param firstLayer
     * @param snoozedActions
     * @return
     */
  /*  private SolutionExecutionEvent applySolution(Date newDate, ConflictSolution potentialSolution, HashMap<String, HassioState> lastStates, List<HassioState> firstLayer, List<ConflictingAction> snoozedActions) {
        SolutionExecutionEvent solutionExecution = new SolutionExecutionEvent(potentialSolution.solutionID, newDate);

        // Snooze actions, if any
        snoozedActions.addAll(potentialSolution.snoozedActions);

        // simulate custom actions, if any
        for(Action customAction : potentialSolution.customActions) {
            List<HassioState> solutionStates = customAction.simulate(newDate, lastStates);
            solutionExecution.addActionExecuted(customAction.id, solutionStates);

            for(HassioState solutionState : solutionStates) {
                firstLayer.addHassioState(new HassioState(solutionState, solutionExecution));
            }
        }

        return solutionExecution;
    } */

    /**
     * Find solutions and apply them if possible
     * @param newDate
     * @param conflictsMapping
     * @param lastStates
     * @param firstLayer
     * @param snoozedActions
     * @param future
     */
  /*  private void applySolution(Date newDate, HashMap<String, List<Conflict>> conflictsMapping, HashMap<String, HassioState> lastStates, List<HassioState> firstLayer, List<ConflictingAction> snoozedActions, Future future, boolean[] flags) {
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
    } */


    /**


    /**
     * Determines if state is part of a conflict
     * @return true when state is part of a conflict
     */
  /*  private boolean isConflictingstate(HassioState state, List<Conflict> conflicts) {
        for (Conflict conflict : conflicts) {
            if (conflict.conflictingEntities.contains(state.entity_id) || conflict.conflictingEntities.contains("Loop_"+ state.entity_id)) {
                return true;
            }
        }
        return false;
    } */

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

    /**
     * Submit states to the future one by one and check if they will trigger a conflict
     * @param newDate
     * @param future
     * @param newLayer
     * @return
     */
    private List<Conflict> submitStatesToFuture(Date newDate, Future future, List<HassioState> newLayer) {
        List<Conflict> conflicts = new ArrayList<>();

        this.printLayer(newDate, newLayer);

        for(HassioState newState : newLayer) {
            List<Conflict> additionalConflicts = this.conflictVerificationManager.verifyConflicts(newDate, future, newState);
            future.addFutureState(newState);
            conflicts.addAll(additionalConflicts);
        }

        return conflicts;
    }

    private void printLayer(Date date, List<HassioState> HassioStates) {
        if(HassioStates.isEmpty()) return;

        if(this.lastLoggedDate != null && this.lastLoggedDate.getTime() == date.getTime()) {
            System.out.print("                          -> : ");
        } else {
            System.out.print(date + ": ");
        }

        for(HassioState HassioState : HassioStates) {
            System.out.print(HassioState.entity_id + " = " + HassioState.state + ", ");
        }

        System.out.println();
        this.lastLoggedDate = date;
    }
}