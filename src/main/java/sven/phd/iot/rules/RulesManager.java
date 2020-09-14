package sven.phd.iot.rules;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.triggers.NeverTrigger;

import java.util.*;
import java.util.List;

public class RulesManager {
    public static final String RULE_IMPLICIT_BEHAVIOR = "rule.implicit_behavior";

    private Map<String, Trigger> rules;

    public RulesManager() {
        System.out.println("RulesManager - Initiating...");
        this.rules = new HashMap<>();

        // Implicit behavior needs to be collected, but should remain hidden
       addRule(new NeverTrigger(RULE_IMPLICIT_BEHAVIOR, "'Hacky' rule that represents implicit behavior from other devices"));

        //Load Bram's rules
        //this.rules.putAll(BramRulesManager.getRules());
    }

    /**
     * Rules could be triggered by multiple triggers in the same "tick"
     * @param date
     * @param hassioChanges
     * @param simulatedRulesEnabled
     * @return
     */
    public List<RuleExecution> verifyTriggers(Date date, List<HassioChange> hassioChanges, HashMap<String, Boolean> simulatedRulesEnabled) {
        List<RuleExecution> triggerEvents = new ArrayList<>();

        HashMap<String, List<HassioContext>> ruleContextTriggerMap = new HashMap<>();

        // Find all unique rules that will be triggered
        for(HassioChange hassioChange : hassioChanges) {
            for(String triggerName : this.rules.keySet()) {
                boolean enabled = this.rules.get(triggerName).isEnabled(date);

                if(simulatedRulesEnabled.containsKey(triggerName)) {
                    enabled = simulatedRulesEnabled.get(triggerName);
                }

                if(enabled && this.rules.get(triggerName).isTriggeredBy(hassioChange)) {
                    if(!ruleContextTriggerMap.containsKey(triggerName)) {
                        ruleContextTriggerMap.put(triggerName, new ArrayList<>());
                    }

                    ruleContextTriggerMap.get(triggerName).add(hassioChange.hassioChangeData.newState.context);
                }
            }
        }

        // Create trigger events for each unique rule
        for(String ruleName : ruleContextTriggerMap.keySet()) {
            triggerEvents.add(new RuleExecution(date, ruleName, ruleContextTriggerMap.get(ruleName)));
        }

        return triggerEvents;
    }

    /**
     * Check for every triggerEvent if the condition holds
     * @param states
     * @param triggerEvents
     * @return
     */
    public List<RuleExecution> verifyConditions(HashMap<String, HassioState> states, List<RuleExecution> triggerEvents) {
        List<RuleExecution> filteredTriggerEvents = new ArrayList<>();

        for(RuleExecution triggerEvent : triggerEvents) {
            Trigger rule = this.getRuleById(triggerEvent.ruleID);
            List<HassioState> conditionStates = rule.verifyCondition(states);

            // If the condition was not false
            if(conditionStates != null) {
                for(HassioState conditionState : conditionStates) {
                    triggerEvent.addConditionContext(conditionState.context);
                }

                filteredTriggerEvents.add(triggerEvent);
            }
        }

        return filteredTriggerEvents;
    }

    public List<RuleExecution> getPastRuleExecutions() {
        List<RuleExecution> executions = new ArrayList<>();

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
    public List<RuleExecution> getPastRuleExecutions(String id) {
        List<RuleExecution> executions = new ArrayList<>();

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
        }
    }

    public void setAllRulesEnabled(boolean enabled) {
        for(String deviceID : this.rules.keySet()) {
            rules.get(deviceID).setEnabled(enabled);
        }
    }

    public void addImplicitPrediction(RuleExecution ruleRuleExecution) {
       // rules.get(RULE_IMPLICIT_BEHAVIOR).
    }

    public Map<String, Action> getAllActions() {
        Map<String, Action> result = new HashMap<>();
        for (Trigger rule: rules.values()) {
            List<Action> actions = rule.actions;
            for (Action action: actions) {
                result.put(action.id, action);
            }
        }
        return result;
    }
}