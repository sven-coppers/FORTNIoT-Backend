package sven.phd.iot.predictions;
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
    private Future future;
    private Boolean predicting;
    private long tickRate = 5; // minutes
    private long predictionWindow = 1 * 24 * 60; // 1 day in minutes

    public PredictionEngine(RulesManager rulesManager, HassioDeviceManager hassioDeviceManager, ConflictSolutionManager solutionManager) {
        this.rulesManager = rulesManager;
        this.hassioDeviceManager = hassioDeviceManager;
        this.stateScheduler = hassioDeviceManager.getStateScheduler();
        this.solutionManager = solutionManager;
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
     * TODO: This engine is still sensitive to loops and race conditions
     */
    private Future predictFuture(HashMap<String, Boolean> simulatedRulesEnabled, List<HassioState> simulatedStates) {
        Future future = new Future();
        future.setFutureConflictSolutions(solutionManager.getSolutions());

        // Initialise the queue with changes we already know
        PriorityQueue<HassioState> queue = new PriorityQueue<>();

        HashMap<String, HassioState> lastStates = hassioDeviceManager.getCurrentStates();
        queue.addAll(hassioDeviceManager.predictFutureStates());
        queue.addAll(simulatedStates);
        queue.addAll(stateScheduler.getScheduledStates());

        Date lastFrameDate = new Date(); // Prediction start
        Date predictionEnd = new Date(new Date().getTime() + getPredictionWindow() * 60l * 1000l); // Convert prediction window from minutes to milliseconds

        // Predict the first day with high precision
        while(lastFrameDate.getTime() < predictionEnd.getTime()) {
            Date nextTickDate = new Date(lastFrameDate.getTime() + (getTickRate() * 60l * 1000l)); // Convert tickrate from minutes to milliseconds

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

        // MATHIAS TESTING
        /*
        FutureConflictDetector detector = new FutureConflictDetector();
        future.addFutureConflict(detector.getFutureConflicts(future));
         */

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
        CausalLayer firstLayer = new CausalLayer(); // Never changes, because no rules are executed yet
        boolean runRequired = true;

        // IMPLICIT Let the devices predict their state, based on the past states (e.g. temperature)
        if(this.isPredicting()) {
            firstLayer.addAll(this.verifyImplicitBehavior(newDate, lastStates));
        }

        // BASELINE: Add states from the global queue (before the current date), which could induce conflicts
        while(!globalQueue.isEmpty() && globalQueue.peek().getLastChanged().getTime() <= newDate.getTime()) {
            HassioState newState = globalQueue.poll();

            firstLayer.addState(new CausalNode(newState, null));
        }

        if(!firstLayer.isEmpty()) {
            causalStack.addLayer(firstLayer);
            System.out.println("Tick " + newDate);

            while(runRequired && this.isPredicting()) {
                causalStack = this.deduceStack(newDate, firstLayer, lastStates, simulatedRulesEnabled, snoozedActions); // TODO simulatedRulesEnabled is never used!
                runRequired = this.detectConflicts(newDate, lastStates, causalStack, firstLayer, snoozedActions, future);
            }

            this.commitPredictedStates(causalStack, future);
        }
    }

    /**
     *
     * @param causalStack
     * @param firstLayer (out-parameter) will be extended with custom actions, based on applicable solutions
     * @param snoozedActions (out-parameter) will be extended with snoozedActions, based on applicable solutions
     * @return
     */
    private boolean detectConflicts(Date newDate, HashMap<String, HassioState> lastStates, CausalStack causalStack, CausalLayer firstLayer, List<ConflictingAction> snoozedActions, Future future) {
        if(causalStack.isEmpty()) return false;

        System.out.println("Decting conflicts on the causal stack");
        causalStack.print();

        // Group all potential changes by entityID
        List<CausalNode> potentialChanges = causalStack.flatten();

        for(int i = 0; i < potentialChanges.size(); ++i) {
            List<CausalNode> conflictingChanges = new ArrayList<>();
            String initialEntityID = potentialChanges.get(i).getState().entity_id;

            for (int j = i; j < potentialChanges.size(); ++j) {
                if (potentialChanges.get(j).getState().entity_id.equals(initialEntityID)) {
                    conflictingChanges.add(potentialChanges.get(j));
                }
            }

            if (conflictingChanges.size() > 1) {
                System.out.println("INCONSISTENCY DETECTED FOR " + initialEntityID);
                Conflict newInconsistency = new Conflict(initialEntityID, conflictingChanges);
                ConflictSolution potentialSolution = solutionManager.getSolutionForConflict(newInconsistency);

                if (potentialSolution == null) {
                    future.addFutureConflict(newInconsistency);
                } else {
                    SolutionExecutionEvent solutionExecution = this.applySolution(newDate, potentialSolution, lastStates, firstLayer, snoozedActions);
                    //future.addExecutionEvent(solutionExecution);
                    System.out.println("Solution applied, rerun required");
                    // TODO: A solution should only be applied once?
                    return true;
                }
            }
        }

        return false;
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
    public SolutionExecutionEvent applySolution(Date newDate, ConflictSolution potentialSolution, HashMap<String, HassioState> lastStates, CausalLayer firstLayer, List<ConflictingAction> snoozedActions) {
        SolutionExecutionEvent solutionExecution = new SolutionExecutionEvent(potentialSolution.solutionID, newDate);

        // Snooze actions, if any
        snoozedActions.addAll(potentialSolution.snoozedActions);

        // simulate custom actions, if any
        for(Action customAction : potentialSolution.customActions) {
            List<HassioState> solutionStates = customAction.simulate(newDate, lastStates);
            solutionExecution.addActionExecuted(customAction.id, solutionStates);

            for(HassioState solutionState : solutionStates) {
                firstLayer.addState(new CausalNode(solutionState, solutionExecution));
            }
        }

        return solutionExecution;
    }

    /**
     * Commit the predicted states on the causal stack to the future
     * @param causalStack
     * @param future
     */
    private void commitPredictedStates(CausalStack causalStack, Future future) {
        List<CausalNode> finalNewChanges = causalStack.flatten();

        for(CausalNode node : finalNewChanges) {
            future.addFutureState(node.getState());

            if(node.getExecutionEvent() != null) {
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
    private CausalStack deduceStack(Date newDate, CausalLayer firstLayer, HashMap<String, HassioState> lastStates, HashMap<String, Boolean> simulatedRulesEnabled, List<ConflictingAction> snoozedActions) {
        System.out.println("Deducing new causal stack");
        CausalStack causalStack = new CausalStack();
        causalStack.addLayer(firstLayer);
        CausalLayer newLayer = firstLayer;

        // Determine future (could contain inconsistencies and loops)
        while (!newLayer.isEmpty()) {
            newLayer = deduceLayer(newDate, causalStack, lastStates, simulatedRulesEnabled, snoozedActions);

            if (!newLayer.isEmpty()) {
                // TODO Do loop detection, adapt layer and then add to stack
                causalStack.addLayer(newLayer);
            }
        }

        return causalStack;
    }

    /**
     * Determine which new states would occur (in a new layer) based on the new states from the previous layer)
     * @param newDate
     * @param causalStack
     * @param lastStates
     * @param simulatedRulesEnabled
     * @param snoozedActions the actions which are snoozed (e.g. by conflict solutions)
     * @return
     */
    private CausalLayer deduceLayer(Date newDate, CausalStack causalStack, HashMap<String, HassioState> lastStates, HashMap<String, Boolean> simulatedRulesEnabled, List<ConflictingAction> snoozedActions) {
        // Build the states for this layer
        HashMap<String, HassioState> layerSpecificStates = buildLayerSpecificStates(lastStates, causalStack);

        CausalLayer previousLayer = causalStack.getTopLayer();
        CausalLayer newLayer = new CausalLayer();

        // Build a list of changes in this layer
        List<HassioChange> newChanges = new ArrayList<>();
        for(int i = 0; i < previousLayer.getNumStates(); ++i) {
            CausalNode node = previousLayer.getState(i);

            HassioState newState = node.getState();
            HassioState lastState = lastStates.get(newState.entity_id);

            newChanges.add(new HassioChange(newState.entity_id, lastState, newState, newState.getLastChanged()));
        }

        // Pass the stateChange to the set of rules and to the implicit behavior
        newLayer.addAll(this.verifyExplicitRules(newDate, newChanges, layerSpecificStates, simulatedRulesEnabled, snoozedActions));
        newLayer.addAll(this.verifyImplicitBehavior(newDate, layerSpecificStates));

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
    private List<CausalNode> verifyExplicitRules(Date date, List<HassioChange> newChanges, HashMap<String, HassioState> states, HashMap<String, Boolean> simulatedRulesEnabled, List<ConflictingAction> snoozedActions) {
        List<CausalNode> result = new ArrayList<>();

        List<RuleExecutionEvent> triggerEvents = this.rulesManager.verifyTriggers(date, newChanges, new HashMap<>());
        List<RuleExecutionEvent> conditionTrueEvents = this.rulesManager.verifyConditions(states, triggerEvents);

        for(RuleExecutionEvent potentialExecutionEvent : conditionTrueEvents) {
            // Find which actions should be snoozed for this rule
            List<String> ruleSpecificSnoozedActions = new ArrayList<>();
            for(ConflictingAction conflictingAction : snoozedActions) {
                if(conflictingAction.rule_id.equals(potentialExecutionEvent.getTrigger().id)) ruleSpecificSnoozedActions.add(conflictingAction.action_id);
            }

            HashMap<String, List<HassioState>> proposedActionState = potentialExecutionEvent.getTrigger().simulate(potentialExecutionEvent, states, ruleSpecificSnoozedActions);

            // For every proposed action, add it to the queue
            for(String actionID : proposedActionState.keySet()) {
                for(HassioState proposedState : proposedActionState.get(actionID)) {
                    result.add(new CausalNode(proposedState, potentialExecutionEvent));
                }
            }

            // Add the context of the simulated actions as a result in the potentialTriggerEvent
            for(String actionID : proposedActionState.keySet()) {
                potentialExecutionEvent.addActionExecuted(actionID, proposedActionState.get(actionID));
            }
        }

        return result;
    }

    /**
     * Pass the stateChange to the implicit rules (e.g. turn heater on/off)
     * @param states
     * @return
     */
    private List<CausalNode> verifyImplicitBehavior(Date newDate, HashMap<String, HassioState> states) {
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
     * Build a hasmap with the last states, updated with all changes that are specific to this branch in the three
     * @param lastStates
     * @return
     */
    private HashMap<String, HassioState> buildLayerSpecificStates(HashMap<String, HassioState> lastStates, CausalStack causalStack) {
        HashMap<String, HassioState> branchSpecificStates = new HashMap<>();

        for(String deviceID : lastStates.keySet()) {
            branchSpecificStates.put(deviceID, lastStates.get(deviceID));
        }

        for(int i = 0; i < causalStack.getNumLayers(); ++i) {
            for(CausalNode causalNode : causalStack.getLayer(i).getStates()) {
                branchSpecificStates.put(causalNode.getState().entity_id, causalNode.getState());
            }
        }

        return branchSpecificStates;
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