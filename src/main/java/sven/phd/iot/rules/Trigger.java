package sven.phd.iot.rules;


import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;

import java.util.*;

abstract public class Trigger {
    private List<HassioRuleExecutionEvent> executionHistory;
    private List<Action> actions;
    protected String title;
    public String id;
    private long offset;
    protected boolean enabled; // Is the rule enabled. When the rule is disabled, it cannot be triggered

    public Trigger(String id, String title) {
        this.executionHistory = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.title = title;
        this.id = id;
        this.offset = 0;
        this.enabled = true;
    }

    /**
     * Get the execution history of this rule
     */
    public List<HassioRuleExecutionEvent> getExecutionHistory() {
        return this.executionHistory;
    }

    /**
     * Add an action that needs to be executed when the trigger is satisfied
     * @param action
     */
    public void addAction(Action action) {
        this.actions.add(action);
    }

    /**
     * Check when the rule will trigger, based on specified hassioChanges
     * @param hassioStates the state at that time
     * @param hassioChange the change that caused this rule to be validated
     * @return
     */
    public HassioRuleExecutionEvent verify(HashMap<String, HassioState> hassioStates, HassioChange hassioChange) {
        if(this.enabled && this.isTriggeredBy(hassioChange)) { // TODO: Allow enabled to be simulated
            // Check if the rule would be triggered by this change (AND WHY)
            List<HassioContext> triggerContexts = this.verifyCondition(hassioStates);

            if(triggerContexts != null) {
                // The rule is triggered
                HassioRuleExecutionEvent newEvent = new HassioRuleExecutionEvent(this, hassioChange.datetime, this.offset);
                newEvent.addTriggerContexts(triggerContexts);

                return newEvent;
            } else {
                // The rule is not triggered
            }
        }

        return null;
    }

    public void logHassioRuleExecutionEvent(HassioRuleExecutionEvent hassioRuleExecutionEvent) {
        this.executionHistory.add(hassioRuleExecutionEvent);
    }

    /**
     * Check if the rule is interested in being verified after this change
     * @param hassioChange the change that this rule might be interested in
     * @return true if the rule is interested, false otherwise
     * SHOULD ONLY BE CALLED BY THE RULE ITSELF
     */
    protected abstract boolean isTriggeredBy(HassioChange hassioChange);

    /**
     * Check if the hassioChange causes this trigger to be triggered
     * @param hassioStates a map with states for each device
     * @return a list of HassioContexts that trigger the rule, returns null when the rule it NOT triggered, returns an empty list when the rule is triggered by itself
     */
    protected abstract List<HassioContext> verifyCondition(HashMap<String, HassioState> hassioStates);

    /**
     * Run all actions and collect all states that would result from it
     * @return
     */
    public List<HassioState> simulate(HassioRuleExecutionEvent executionEvent) {
        List<HassioState> results = new ArrayList<>();

        for(Action action : this.actions) {
            results.addAll(action.simulate(executionEvent));
        }

        return results;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
}