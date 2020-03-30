package sven.phd.iot.predictions;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.students.mathias.states.HassioConflictState;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Future {
    @JsonProperty("states") private List<HassioState> futureStates;
    @JsonProperty("executions") public List<HassioRuleExecutionEvent> futureExecutions;
    @JsonProperty("conflicts") public List<HassioConflictState> futureConflicts;
    @JsonProperty("last_generated") public Date lastGenerated;

    public Future() {
        this.futureStates = new ArrayList<>();
        this.futureExecutions = new ArrayList<>();
        this.futureConflicts = new ArrayList<>();
        this.lastGenerated = new Date();
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
     * Get a cached version of the prediction of the future conflicts
     */
    public List<HassioConflictState> getFutureConflicts() {
        return this.futureConflicts;
    }

    /**
     * Get a cached version of the prediction of the future conflicts for a single device
     */
    public List<HassioConflictState> getFutureConflicts(String deviceID) {
        List<HassioConflictState> result = new ArrayList<>();

        for(HassioConflictState hassioConflict : this.futureConflicts) {
            if(hassioConflict.entity_id.equals(deviceID)) {
                result.add(hassioConflict);
            }
        }

        return result;
    }

    /**
     * Add a future conflict to the list of predictions
     * @param newConflict
     */
    public void addFutureConflict(HassioConflictState newConflict) {
        this.futureConflicts.add(newConflict);
    }

    /**
     * Add future conflicts to the list of predictions
     * @param newConflicts
     */
    public void addFutureConflict(List<HassioConflictState> newConflicts) {this.futureConflicts = newConflicts; }
}