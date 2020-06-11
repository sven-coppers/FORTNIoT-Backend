package sven.phd.iot.predictions;
import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.hassio.updates.ImplicitBehaviorEvent;
import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.students.mathias.FutureConflictDetector;
import sven.phd.iot.students.mathias.states.Conflict;
import sven.phd.iot.students.mathias.states.ConflictSolution;
import sven.phd.iot.students.mathias.states.ConflictingAction;

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
        FutureConflictDetector detector = new FutureConflictDetector();
        future.addFutureConflict(detector.getFutureConflicts(future));

        return future;
    }

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
        }

        while(runRequired && this.isPredicting()) {
            if(firstLayer.isEmpty()) break;

            System.out.println("- Running the predictions");
            causalStack = new CausalStack();
            causalStack.addLayer(firstLayer);
            CausalLayer newLayer = firstLayer;
            runRequired = false;

            // Determine future (could contain inconsistencies
            while (!newLayer.isEmpty()) {
                newLayer = deduceLayer(newDate, causalStack, lastStates, simulatedRulesEnabled, snoozedActions);

                if (!newLayer.isEmpty()) {
                    causalStack.addLayer(newLayer);
                }
            }

            if (!causalStack.isEmpty()) {
                causalStack.print();
            }

            // Group all potential changes by entityID
            HashMap<String, List<CausalNode>> potentialChanges = new HashMap<>();

            for(CausalNode node : causalStack.flatten()) {
                if(potentialChanges.get(node.getState().entity_id) == null) {
                    potentialChanges.put(node.getState().entity_id, new ArrayList<>());
                }

                potentialChanges.get(node.getState().entity_id).add(node);
            }

            // Check if there are multiple potential states for the same entityID (inconsistency)
            for(String entityID : potentialChanges.keySet()) {
                if(potentialChanges.get(entityID).size() > 1) {
                    System.out.println("INCONSISTENCY DETECTED FOR " + entityID);
                    Conflict newInconsistency = new Conflict(entityID);

                    // Build the conflict
                    for(CausalNode node : potentialChanges.get(entityID)) {
                        if(node.getExecutionEvent() == null) {
                            System.err.println("The conflicting state did not have an execution event for " + node.getState().entity_id + " = " + node.getState().state);
                            continue;
                        }

                        String causingAction = node.getExecutionEvent().getResponsibleAction(node.getState().context);

                        if(causingAction == null) {
                            // The state is not caused by a rule
                            System.err.println("The conflicting state is not caused by a rule");
                        }

                        newInconsistency.addAction(new ConflictingAction(causingAction, node.getExecutionEvent().getTrigger().id));
                    }

                    ConflictSolution potentialSolution = solutionManager.getSolutionForConflict(newInconsistency);

                    if(potentialSolution == null) {
                        future.addFutureConflict(newInconsistency);
                    } else {
                        // Apply Solutions
                        System.out.println("Solution applied, rerun required");

                        snoozedActions.addAll(potentialSolution.snoozedActions);
                        // TODO: What about custom actions?
                        runRequired = true;
                        break; // One solution at a time
                    }
                }
            }
        }

        // Overgebleven states committen we als predicted states
        List<CausalNode> finalNewChanges = causalStack.flatten();

        for(CausalNode node : finalNewChanges) {
            future.addFutureState(node.getState());

            if(node.getExecutionEvent() != null) {
                future.addHassioRuleExecutionEventPrediction(node.getExecutionEvent());
            }
        }
    }

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
     * @return
     */
    private List<CausalNode> verifyExplicitRules(Date date, List<HassioChange> newChanges, HashMap<String, HassioState> states, HashMap<String, Boolean> simulatedRulesEnabled, List<ConflictingAction> snoozedActions) {
        List<CausalNode> result = new ArrayList<>();

        List<HassioRuleExecutionEvent> triggerEvents = this.rulesManager.verifyTriggers(date, newChanges, new HashMap<>());
        List<HassioRuleExecutionEvent> conditionTrueEvents = this.rulesManager.verifyConditions(states, triggerEvents);

        for(HassioRuleExecutionEvent potentialExecutionEvent : conditionTrueEvents) {
            // Find which actions should be snoozed for this rule
            List<String> ruleSpecificSnoozedActions = new ArrayList<>();
            for(ConflictingAction conflictingAction : snoozedActions) {
                if(conflictingAction.rule_id == potentialExecutionEvent.getTrigger().id) ruleSpecificSnoozedActions.add(conflictingAction.action_id);
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
                List<HassioContext> resultingContexts = new ArrayList<>();

                for(HassioState proposedState : proposedActionState.get(actionID)) {
                    resultingContexts.add(proposedState.context);
                }

                potentialExecutionEvent.addActionExecuted(actionID, resultingContexts);
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