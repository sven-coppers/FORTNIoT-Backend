package sven.phd.iot.rules;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;
import sven.phd.iot.hassio.states.HassioState;

import java.util.*;

public class RuleExecution implements Comparable<RuleExecution> {
    private static int identifier = 0; // Increased every time it is used

    @JsonProperty("rule_id") public String ruleID; // WHICH RULE WAS EXECUTED?
    @JsonProperty("execution_id") public String execution_id; // AUTOMATICALLY GENERATED ID

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("datetime") public Date datetime;

    @JsonProperty("trigger_context") public List<HassioContext> triggerContexts;
    @JsonProperty("condition_satisfying_contexts") public List<HassioContext> conditionContexts; // HashMap "ConditionID" => List of contexts (condition satisfying states)
    @JsonProperty("action_executions") public List<ActionExecution> actionExecutions;

    public RuleExecution(Date datetime) {
        this(datetime, RulesManager.RULE_IMPLICIT_BEHAVIOR, new ArrayList<>());
    }

    public RuleExecution(Date datetime, String ruleID, List<HassioContext> triggerContexts) {
        this.ruleID = ruleID;
        this.datetime = datetime;
        this.triggerContexts = triggerContexts;
        this.conditionContexts = new ArrayList<>();
        this.actionExecutions = new ArrayList<>();
        this.execution_id = "rule_execution_" + identifier++;
        //this.execution_id = UUID.randomUUID().toString();
    }

    /**
     * Constructor meant for devices that update themselves
    // * @param datetime
     */
 /*   public ExecutionEvent(Date datetime, String actionDeviceID) {
        super(RulesManager.RULE_IMPLICIT_BEHAVIOR, datetime);
        triggerDevicesIDs = new ArrayList<>();
        actionDevicesIDs = new HashMap<>();
        actionStates = new ArrayList<>();

        this.addActionDeviceID(actionDeviceID);
        this.addTriggerDeviceID(actionDeviceID);
    } */




 /*   public void addTriggerDeviceID(String triggerDevice) {
        this.triggerDevicesIDs.add(triggerDevice);
    }

    public List<String> getTriggerDeviceIDs() {
        return this.triggerDevicesIDs;
    } */

    /*public void resolveContextIDs(HashMap<String, HassioState> hassioStates) {
        for(String deviceID: this.triggerDevicesIDs) {
            this.addConditionContext(deviceID, hassioStates.get(deviceID).context);
        }

        for(String actionID : this.actionDevicesIDs.keySet()) {
            for(String deviceID: this.actionDevicesIDs.get(actionID)) {
                this.addActionExecuted(actionID, hassioStates.get(deviceID));
                this.actionStates.add(hassioStates.get(deviceID));
            }
        }
    } */



    public void addTriggerContext(HassioContext context) {
        this.triggerContexts.add(context);
    }

    public List<ActionExecution> getActionExecutions() {
        return this.actionExecutions;
    }

    public void addConditionContext(HassioContext context) {
        this.conditionContexts.add(context);
    }

    public boolean isInConditionSatisfied(HassioContext context) {
        // TODO: Fix
     /*   for (String key: conditionContexts.keySet()) {
            if (conditionContexts.get(key).contains(context)) {
                return true;
            }
        } */
        return false;
    }

    public void addActionExecution(ActionExecution actionExecution) {
        this.actionExecutions.add(actionExecution);
    }



  /*  public String getResponsibleAction(HassioContext findContext) {
        for(String actionID : this.actionContexts.keySet()) {
            for(HassioContext haystackContext : this.actionContexts.get(actionID)) {
                if(haystackContext.id.equals(findContext.id)) {
                    return actionID;
                }
            }
        }

        return null;
    } */

    public int compareTo(RuleExecution hassioEvent) {
        if(hassioEvent.datetime == null || this.datetime == null) {
            System.out.println();
        }
        return this.datetime.compareTo(hassioEvent.datetime);
    }
}
