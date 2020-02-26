package sven.phd.iot.rules;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;

import java.util.*;
import java.util.List;

public class RulesManager {
    private Map<String, Trigger> rules;

    public RulesManager() {
        System.out.println("RulesManager - Initiating...");
        this.rules = new HashMap<>();

        //Load Bram's rules
        //this.rules.putAll(BramRulesManager.getRules());
    }

    /**
     * Check when the rules will trigger
     * @param hassioStates the expected states at that time
     * @param hassioChange the change that caused the rules to be validated
     * @return
     */
    public List<HassioRuleExecutionEvent> verifyTriggers(HashMap<String, HassioState> hassioStates, HassioChange hassioChange) {
        return this.verifyTriggers(hassioStates, hassioChange, new HashMap<>());
    }

    /**
     * Check when the rules will trigger
     * @param hassioStates the expected states at that time
     * @param hassioChange the change that caused the rules to be validated
     * @param simulatedRulesEnabled simulate whether some rules are enbaled/disabled
     * @return
     */
    public List<HassioRuleExecutionEvent> verifyTriggers(HashMap<String, HassioState> hassioStates, HassioChange hassioChange, HashMap<String, Boolean> simulatedRulesEnabled) {
        List<HassioRuleExecutionEvent> triggerEvents = new ArrayList<>();

        for(String triggerName : this.rules.keySet()) {
            boolean enabled = this.rules.get(triggerName).enabled;

            if(simulatedRulesEnabled.containsKey(triggerName)) {
                enabled = simulatedRulesEnabled.get(triggerName);
            }

            HassioRuleExecutionEvent newEvent = this.rules.get(triggerName).verify(hassioStates, hassioChange, enabled);

            if(newEvent != null) {
                triggerEvents.add(newEvent);
            }
        }

        return triggerEvents;
    }

    public List<HassioRuleExecutionEvent> getPastRuleExecutions() {
        List<HassioRuleExecutionEvent> executions = new ArrayList<>();

        for(String triggerName : this.rules.keySet()) {
            executions.addAll(this.rules.get(triggerName).getExecutionHistory());
        }

        Collections.sort(executions);

        return executions;
    }

    /**
     * Get the history states of each device
     * @return
     */
    public List<HassioRuleExecutionEvent> getPastRuleExecutions(String id) {
        List<HassioRuleExecutionEvent> executions = new ArrayList<>();

        if(this.rules.containsKey(id)) {
            executions.addAll(this.rules.get(id).getExecutionHistory());
        }

        Collections.sort(executions);

        return executions;
    }

    public Trigger getRule(String id) {
        return this.rules.get(id);
    }

    public String printRulesToString() {
        String result = "";

        for(String triggerName : this.rules.keySet()) {
            result += this.rules.get(triggerName).toString() + "\n";
        }

        return result;
    }

    public Map<String, Trigger> getRules() {
        return this.rules;
    }

    public Trigger getRuleById(String id) {
        return rules.get(id);
    }

    public void setAllRulesAvailable(boolean available) {
        for(String deviceID : this.rules.keySet()) {
            rules.get(deviceID).setAvailable(available);
        }
    }

    public void addRule(Trigger trigger) {
        this.rules.put(trigger.id, trigger);
    }

    public void deleteRule(String triggerID) {
        this.rules.remove(triggerID);
    }

    public void clearRules() {
        this.rules.clear();
    }

    public void setRuleEnabled(String ruleID, boolean enabled) {
        if(rules.containsKey(ruleID)) {
            rules.get(ruleID).setEnabled(enabled);
        }
    }

    public void setRuleAvailable(String ruleID, boolean available) {
        if(rules.containsKey(ruleID)) {
            rules.get(ruleID).setAvailable(available);

            // It would be confusing if a rule is unavailable to the user, but still active
            if(available == false) {
                this.setRuleEnabled(ruleID, false);
            }
        }
    }

    public void setAllRulesEnabled(boolean enabled) {
        for(String deviceID : this.rules.keySet()) {
            rules.get(deviceID).setEnabled(enabled);
        }
    }
}