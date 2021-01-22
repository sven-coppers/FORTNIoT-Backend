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

        return newDate;
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
            }

            if(this.isPredicting()) {
                lastLayer = deduceLayer(newDate, lastLayer, future, predictionInput);
            } else {
                lastLayer.clear();
            }
        }
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

            // If the newState did not cause any detrimental conflicts --> add it to the future.
            if(!hasDetrimentalConflict(additionalConflicts)) {
                future.addFutureState(newState);
            } else {
                System.out.println("Detrimental conflict detected");
            }

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

    private boolean hasDetrimentalConflict(List<Conflict> conflicts) {
        for(Conflict conflict : conflicts) {
            if(conflict.isDetrimental()) {
                return true;
            }
        }

        return false;
    }
}