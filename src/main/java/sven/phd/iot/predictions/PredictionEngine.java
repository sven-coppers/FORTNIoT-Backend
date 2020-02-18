package sven.phd.iot.predictions;
import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.RulesManager;

import java.util.*;

public class PredictionEngine {
    private HassioStateScheduler stateScheduler;
    private RulesManager rulesManager;
    private HassioDeviceManager hassioDeviceManager;
    private Future future;
    private Boolean predicting;
    private final long tickRate = 5; // minutes

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
        Date lastFrameDate = null;

        while(!queue.isEmpty()) {
            // Add intermediate ticks if needed
            if(this.isPredicting()) {
                HassioState potentialNewState = queue.peek();

                if(lastFrameDate != null) {
                    Date nextFrameDate = new Date(lastFrameDate.getTime() + (tickRate * 60l * 1000l));

                    if(nextFrameDate.getTime() < potentialNewState.last_changed.getTime()) {
                        queue.addAll(hassioDeviceManager.adaptStateToContext(nextFrameDate, lastStates));
                        continue; // Skip this frame
                    }
                }
            }

            HassioState newState = queue.poll();

            // Log the predicted state in the device
            future.addFutureState(newState);
            lastFrameDate = newState.last_changed;

            // Only when additional predictions are enabled
            if(this.isPredicting()) {
                HassioState lastState = lastStates.get(newState.entity_id);
                HassioChange newChange = new HassioChange(newState.entity_id, lastState, newState, newState.last_changed);
                lastStates.put(newState.entity_id, newState);

                // Pass the stateChange to the set of rules
                List<HassioRuleExecutionEvent> triggerEvents = this.rulesManager.verifyTriggers(lastStates, newChange, simulatedRulesEnabled);

                for(HassioRuleExecutionEvent triggerEvent : triggerEvents) {
                    List<HassioState> resultingActions = triggerEvent.getTrigger().simulate(triggerEvent, lastStates);
                    List<HassioContext> resultingContexts = new ArrayList<>();

                    // Add the context of the simulated actions as a result in the triggerEvent
                    for(HassioState resultingAction : resultingActions) {
                        resultingContexts.add(resultingAction.context);
                    }

                    triggerEvent.addActionContexts(resultingContexts);

                    // Add predicted executions to the rule's prediction list
                    future.addHassioRuleExecutionEventPrediction(triggerEvent);

                    // Add the actions to the prediction QUEUEs
                    queue.addAll(resultingActions);
                }
            }
        }

        return future;
    }

   /* private void removePredictionsFromQueue(PriorityQueue<HassioState> queue, String deviceID) {
        for(HassioState state : queue) {
            if(state.entity_id.equals(deviceID)) {
                queue.remove(state);
            }
        }
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
}