package sven.phd.iot.predictions;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Future {
    private List<HassioState> futureStates;
    private List<HassioRuleExecutionEvent> futureExecutions;
    private Date lastGenerated;

    public Future() {
        this.futureStates = new ArrayList<>();
        this.futureExecutions = new ArrayList<>();
        this.lastGenerated = new Date();
    }

    /**
     * Get the execution history of this rule
     */
    public List<HassioRuleExecutionEvent> getExecutionFuture() {
        return this.futureExecutions;
    }

    /**
     * Get the execution history of this rule
     */
    public List<HassioRuleExecutionEvent> getExecutionFuture(String ruleID) {
        List<HassioRuleExecutionEvent> result = new ArrayList<>();

        for(HassioRuleExecutionEvent ruleExecutionEvent : this.futureExecutions) {
            if(ruleExecutionEvent.getTrigger().id.equals(ruleID)) {
                result.add(ruleExecutionEvent);
            }
        }

        return result;
    }

    /**
     * Allow the prediction engine to add a predicted triggerEvent of this rule
     * @param triggerEvent
     */
    public void addHassioRuleExecutionEventPrediction(HassioRuleExecutionEvent triggerEvent) {
        this.futureExecutions.add(triggerEvent);
    }

    /**
     * Get a cached version of the prediction of the future states
     */
    public List<HassioState> getFutureStates() {
        return this.futureStates;
    }

    /**
     * Get a cached version of the prediction of the future states for a single device
     */
    public List<HassioState> getFutureStates(String deviceID) {
        List<HassioState> result = new ArrayList<>();

        for(HassioState hassioState : this.futureStates) {
            if(hassioState.entity_id.equals(deviceID)) {
                result.add(hassioState);
            }
        }

        return result;
    }

    /**
     * Add a future state to the list of predictions
     * @param newState
     */
    public void addFutureState(HassioState newState) {
        this.futureStates.add(newState);
    }

    /**
     * Clear the cache of predictions
     */
    public void clearPredictions() {
        this.futureExecutions.clear();
        this.futureStates.clear();
        this.lastGenerated = new Date();
    }
}