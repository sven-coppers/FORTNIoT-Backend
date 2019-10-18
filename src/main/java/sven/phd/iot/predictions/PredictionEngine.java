package sven.phd.iot.predictions;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.RulesManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class PredictionEngine {
    private RulesManager rulesManager;
    private HassioDeviceManager hassioDeviceManager;

    public PredictionEngine(RulesManager rulesManager, HassioDeviceManager hassioDeviceManager) {
        this.rulesManager = rulesManager;
        this.hassioDeviceManager = hassioDeviceManager;
    }

    /**
     * Predict the future states and event of each HassioDevice and each rule
     * @post: Each HassioDevice and Each Rule will have a cached version of the outcome
     * TODO: This engine is still sensitive to loops and race conditions
     */
    public void predictFuture() {
        this.clearPredictions();

        // Initialise the queue with changes we already know
        PriorityQueue<HassioState> queue = new PriorityQueue<>();
        HashMap<String, HassioState> lastStates = hassioDeviceManager.getCurrentStates();
        queue.addAll(hassioDeviceManager.predictFutureStates());

        while(!queue.isEmpty()) {
            HassioState newState = queue.poll();

            // Log the predicted state in the device
            hassioDeviceManager.getDevice(newState.entity_id).addFutureState(newState);

            HassioState lastState = lastStates.get(newState.entity_id);
            HassioChange newChange = new HassioChange(newState.entity_id, lastState, newState, newState.last_changed);
            lastStates.put(newState.entity_id, newState);

            // Pass the stateChange to the set of rules
            List<HassioRuleExecutionEvent> triggerEvents = this.rulesManager.verifyTriggers(lastStates, newChange);

            for(HassioRuleExecutionEvent triggerEvent : triggerEvents) {
                List<HassioState> resultingActions = triggerEvent.getTrigger().simulate(triggerEvent);
                List<HassioContext> resultingContexts = new ArrayList<>();

                // Add the context of the simulated actions as a result in the triggerEvent
                for(HassioState resultingAction : resultingActions) {
                    resultingContexts.add(resultingAction.context);
                }

                triggerEvent.addActionContexts(resultingContexts);

                // Add predicted rules to the rule's prediction list
                triggerEvent.getTrigger().addHassioRuleExecutionEventPrediction(triggerEvent);

                // Add the actions to the prediction QUEUEs
                queue.addAll(resultingActions);
            }
        }
    }

    private void clearPredictions() {
        // Clear the future of each device
        this.hassioDeviceManager.clearPredictions();

        // Clear the future of each rule
        this.rulesManager.clearPredictions();
    }
}