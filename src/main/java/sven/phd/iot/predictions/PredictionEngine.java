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

import java.util.*;

public class PredictionEngine {
    private HassioStateScheduler stateScheduler;
    private RulesManager rulesManager;
    private HassioDeviceManager hassioDeviceManager;
    private Future future;
    private Boolean predicting;
    private long tickRate = 5; // minutes
    private long predictionWindow = 1 * 24 * 60; // 1 day in minutes

    public PredictionEngine(RulesManager rulesManager, HassioDeviceManager hassioDeviceManager) {
        this.rulesManager = rulesManager;
        this.hassioDeviceManager = hassioDeviceManager;
        this.stateScheduler = hassioDeviceManager.getStateScheduler();
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
        // Let the devices predict their state, based on the past states (e.g. temperature)
        if(this.isPredicting()) {
            List<ImplicitBehaviorEvent> behaviorEvents = hassioDeviceManager.predictImplictStates(newDate, lastStates);

            // Add changes to prediction queue
            for(ImplicitBehaviorEvent behaviorEvent : behaviorEvents) {
                for(HassioState newState : behaviorEvent.getActionStates()) {
                    globalQueue.add(newState);
                }

                // Add Implicit behavior to the future
                behaviorEvent.setTrigger(this.rulesManager.getRuleById(this.rulesManager.RULE_IMPLICIT_BEHAVIOR));
                future.addHassioRuleExecutionEventPrediction((HassioRuleExecutionEvent) behaviorEvent);
            }
        }

        // Remove every State from the global queue that has occurred before the new Date
        while(!globalQueue.isEmpty() && globalQueue.peek().getLastChanged().getTime() <= newDate.getTime()) {
            HassioState newState = globalQueue.poll();

            // Log the predicted state in the device
            future.addFutureState(newState);

            // Only when additional predictions are enabled
            if(this.isPredicting()) {
                HassioState lastState = lastStates.get(newState.entity_id);
                HassioChange newChange = new HassioChange(newState.entity_id, lastState, newState, newState.getLastChanged());
                lastStates.put(newState.entity_id, newState);

                // Pass the stateChange to the set of rules
                List<HassioRuleExecutionEvent> triggerEvents = this.rulesManager.verifyTriggers(lastStates, newChange, simulatedRulesEnabled);

                for(HassioRuleExecutionEvent triggerEvent : triggerEvents) {
                    HashMap<String, List<HassioState>> resultingActions = triggerEvent.getTrigger().simulate(triggerEvent, lastStates);

                    // Add the context of the simulated actions as a result in the triggerEvent
                    for(String actionID : resultingActions.keySet()) {
                        List<HassioContext> resultingContexts = new ArrayList<>();

                        for(HassioState resultingAction : resultingActions.get(actionID)) {
                            resultingContexts.add(resultingAction.context);
                        }

                        triggerEvent.addActionExecuted(actionID, resultingContexts);
                    }

                    // Add predicted executions to the rule's prediction list
                    future.addHassioRuleExecutionEventPrediction(triggerEvent);

                    // Add the actions to the prediction QUEUEs
                    for(String actionID : resultingActions.keySet()) {
                        globalQueue.addAll(resultingActions.get(actionID));
                    }
                }

                // Pass the stateChange to the implicit rules (e.g. turn heater on/off)
                List<ImplicitBehaviorEvent> behaviorEvents = hassioDeviceManager.predictImplicitRules(newDate, lastStates);

                // Add changes to prediction queue
                for(ImplicitBehaviorEvent behaviorEvent : behaviorEvents) {
                    for(HassioState newActionState : behaviorEvent.getActionStates()) {
                        globalQueue.add(newActionState);
                    }

                    // Add Implicit behavior to the future
                    behaviorEvent.setTrigger(this.rulesManager.getRuleById(this.rulesManager.RULE_IMPLICIT_BEHAVIOR));
                    future.addHassioRuleExecutionEventPrediction((HassioRuleExecutionEvent) behaviorEvent);
                }
            }
        }
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