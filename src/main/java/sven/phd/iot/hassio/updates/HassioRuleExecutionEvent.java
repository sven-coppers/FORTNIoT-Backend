package sven.phd.iot.hassio.updates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Trigger;

import java.util.*;

public class HassioRuleExecutionEvent extends HassioEvent {
    @JsonProperty("execution_id") public String execution_id;
    @JsonProperty("trigger_contexts") public HashMap<String, List<HassioContext>> triggerContexts;
    @JsonProperty("action_contexts") public HashMap<String, List<HassioContext>> actionContexts;
 //   @JsonProperty("offset") public long offset;
    @JsonIgnore protected Trigger trigger;

    public HassioRuleExecutionEvent(Trigger trigger, Date datetime) {
        super(trigger.id, datetime);

        this.execution_id = UUID.randomUUID().toString();
        this.trigger = trigger;
        this.triggerContexts = new HashMap<>();
        this.actionContexts = new HashMap<>();
   //     this.offset = offset;
    }

    public HassioRuleExecutionEvent(String triggerID, Date datetime) {
        super(triggerID, datetime);

        this.execution_id = UUID.randomUUID().toString();
        this.trigger = null;
        this.triggerContexts = new HashMap<>();
        this.actionContexts = new HashMap<>();
    }

    public void addTriggerContext(String actionID, HassioContext context) {
        if(this.triggerContexts.get(actionID) == null) {
            this.triggerContexts.put(actionID, new ArrayList<>());
        }

        this.triggerContexts.get(actionID).add(context);
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