package sven.phd.iot.rules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.ExecutionEvent;
import sven.phd.iot.hassio.updates.RuleExecutionEvent;

import java.util.*;

abstract public class Trigger {
    @JsonIgnore private List<RuleExecutionEvent> executionHistory;
    @JsonProperty("actions") public List<Action> actions;
    @JsonProperty("description") public String title;
    @JsonProperty("id") public String id;
    private long offset;
    @JsonProperty("enabled") public boolean enabled; // Is the rule enabled. When the rule is disabled, it cannot be triggered
    @JsonProperty("available") public boolean available; // True if the rule should be accessible through the UI

    /* Mathias adding mute properties
    *  It should be possible to have multiple start and stop times, so a list should be kept
     */
    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("start_time_mute") public Date startTimeMute;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("stop_time_mute") public Date stopTimeMute;

    public Trigger(String id, String title) {
        this.executionHistory = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.title = title;
        this.id = id;
        this.offset = 0;
        this.enabled = true;
        this.available = true;
        this.startTimeMute = null;
        this.stopTimeMute = null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addExecution(RuleExecutionEvent executionEvent) {
        this.executionHistory.add(executionEvent);
    }
    /**
     * Get the execution history of this rule
     */
    public List<RuleExecutionEvent> getExecutionHistory() {
        return this.executionHistory;
    }

    /**
     * Add an action that needs to be executed when the trigger is satisfied
     * @param action
     */
    public void addAction(Action action) {
        this.actions.add(action);
    }

    public void logHassioRuleExecutionEvent(RuleExecutionEvent ruleExecutionEvent) {
        this.executionHistory.add(ruleExecutionEvent);
    }

    /**
     * Check if the rule is interested in being verified after this change (e.g. temp update)
     * @param hassioChange the change that this rule might be interested in
     * @return true if the rule is triggered by this changed, false otherwise.
     */

    public abstract boolean isTriggeredBy(HassioChange hassioChange);

    /**
     * Check if the hassioChange causes this trigger to be triggered
     * @param hassioStates a map with states for each device
     * @return a list of HassioContexts that satisfy the condition of the rule, returns null when the rule it NOT triggered, returns an empty list when the rule is satisfied without any states
     */
    public abstract List<HassioState> verifyCondition(HashMap<String, HassioState> hassioStates);

    /**
     * Run all actions and collect all states that would result from it
     * @return a hashmap with actionID -> list of resulting states from this action
     */
    public HashMap<String, List<HassioState>> simulate(ExecutionEvent executionEvent, HashMap<String, HassioState> hassioStates, List<String> snoozedActions) {
        HashMap<String, List<HassioState>> results = new HashMap<>();

        for(Action action : this.actions) {
            if(!snoozedActions.contains(action.id)) {
                results.put(action.id, action.simulate(executionEvent.datetime, hassioStates));
            }
        }

        return results;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /*MATHIAS*/
    public boolean isEnabled(Date currentTime){
        if (startTimeMute != null && currentTime.compareTo(startTimeMute) >= 0 && enabled) {
            enabled = false;
        }
        if (stopTimeMute != null && currentTime.compareTo(stopTimeMute) > 0 && !enabled) {
            enabled = true;
        }
        return enabled;
    }

    @Override
    public String toString() {
        String result = "";

        if(this.enabled) {
            result += "[ENABLED] " + this.id + "\n";
        } else {
            result += "[DISABLED] " + this.id + "\n";
        }

        result += "\t" + this.title + "\n";

        for(Action action : actions) {
            result += "\t\t-> " + action.toString() + "\n";
        }

        return result;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getTitle() {
        return this.title;
    }

    public abstract List<String> getTriggeringEntities();

    public Action getActionOnDevice(String deviceId) {
        for(Action action: actions) {
            if(action.getDeviceID().equals(deviceId)) {
                return action;
            }
        }
        return null;
    }
}