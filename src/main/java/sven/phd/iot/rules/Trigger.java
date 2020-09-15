package sven.phd.iot.rules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;

import java.util.*;

abstract public class Trigger {
    @JsonIgnore private List<RuleExecution> executionHistory;
    @JsonProperty("actions") public List<Action> actions;
    @JsonProperty("description") public String title;
    @JsonProperty("id") public String id;
    private long offset;
    @JsonProperty("enabled") public boolean enabled; // Is the rule enabled. When the rule is disabled, it cannot be triggered
    @JsonProperty("available") public boolean available; // True if the rule should be accessible through the UI

    public Trigger(String id, String title) {
        this.executionHistory = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.title = title;
        this.id = id;
        this.offset = 0;
        this.enabled = true;
        this.available = true;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addExecution(RuleExecution executionEvent) {
        this.executionHistory.add(executionEvent);
    }
    /**
     * Get the execution history of this rule
     * @return
     */
    public Collection<? extends RuleExecution> getExecutionHistory() {
        return this.executionHistory;
    }

    /**
     * Add an action that needs to be executed when the trigger is satisfied
     * @param action
     */
    public void addAction(Action action) {
        this.actions.add(action);
    }

    public void logHassioRuleExecutionEvent(RuleExecution ruleExecutionEvent) {
        this.executionHistory.add(ruleExecutionEvent);
    }

    /**
     * Check if the rule is interested in being verified after this change (e.g. temp update)
     *
     * @param hassioChange the change that this rule might be interested in
     * @return true if the rule is triggered by this changed, false otherwise.
     */

    public abstract boolean isTriggeredBy(HassioChange hassioChange);

    /**
     * Check if the hassioChange causes this trigger to be triggered
     *
     * @param hassioStates a map with states for each device
     * @return a list of HassioContexts that satisfy the condition of the rule, returns null when the rule it NOT triggered, returns an empty list when the rule is satisfied without any states
     */
    public abstract List<HassioContext> verifyCondition(HashMap<String, HassioState> hassioStates);

    /**
     * Run all actions and collect all states that would result from it
     * @return a hashmap with actionID -> list of resulting states from this action
     */
    public HashMap<String, List<HassioState>> simulate(RuleExecution ruleExecution, HashMap<String, HassioState> hassioStates, List<String> snoozedActions) {
        HashMap<String, List<HassioState>> results = new HashMap<>();

        for(Action action : this.actions) {
            if(!snoozedActions.contains(action.id)) {
                results.put(action.id, action.simulate(ruleExecution.datetime, hassioStates));
            }
        }

        return results;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() { return this.enabled; }

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


    public Action getActionOnDevice(String deviceId) {
        for(Action action: actions) {
            if(action.getDeviceID().equals(deviceId)) {
                return action;
            }
        }
        return null;
    }
}