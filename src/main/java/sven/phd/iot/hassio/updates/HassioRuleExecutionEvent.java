package sven.phd.iot.hassio.updates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Trigger;

import java.util.*;

public class HassioRuleExecutionEvent extends HassioEvent {
    @JsonProperty("execution_id") public String execution_id;
    @JsonProperty("condition_satisfying_contexts") public HashMap<String, List<HassioContext>> conditionContexts;
    @JsonProperty("trigger_context") public List<HassioContext> triggerContext;
    @JsonProperty("action_contexts") public HashMap<String, List<HassioContext>> actionContexts;
 //   @JsonProperty("offset") public long offset;
    @JsonIgnore protected Trigger trigger;

    public HassioRuleExecutionEvent(Trigger trigger, Date datetime, List<HassioContext> triggerContext) {
        super(trigger.id, datetime);

        this.execution_id = UUID.randomUUID().toString();
        this.trigger = trigger;
        this.conditionContexts = new HashMap<>();
        this.actionContexts = new HashMap<>();
        this.triggerContext = triggerContext;
   //     this.offset = offset;
    }

    public HassioRuleExecutionEvent(String triggerID, Date datetime) {
        super(triggerID, datetime);

        this.execution_id = UUID.randomUUID().toString();
        this.trigger = null;
        this.conditionContexts = new HashMap<>();
        this.actionContexts = new HashMap<>();
    }

    public void addConditionContext(String actionID, HassioContext context) {
        if(this.conditionContexts.get(actionID) == null) {
            this.conditionContexts.put(actionID, new ArrayList<>());
        }

        this.conditionContexts.get(actionID).add(context);
    }

    public void addActionExecuted(String actionID, List<HassioContext> contexts) {
        if(this.actionContexts.get(actionID) == null) {
            this.actionContexts.put(actionID, new ArrayList<>());
        }

        this.actionContexts.get(actionID).addAll(contexts);
    }

    public Trigger getTrigger() {
        return this.trigger;
    }

    /* Deprecated??? */
    public void resolveContexts(HashMap<String, HassioState> hassioStates, List<String> triggerDevices, List<String> actionDevices) {
    /*    for(String triggerDevice : triggerDevices) {
            this.triggerContexts.add(hassioStates.get(triggerDevice).context);
        } */

        for(String actionDevice : actionDevices) {
            //this.actionContexts.add(hassioStates.get(actionDevice).context); // Incompatible now
        }
    }
}