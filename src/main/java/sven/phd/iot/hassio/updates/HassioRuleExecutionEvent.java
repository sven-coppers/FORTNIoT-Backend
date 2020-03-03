package sven.phd.iot.hassio.updates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Trigger;

import java.util.*;

public class HassioRuleExecutionEvent extends HassioEvent {
    @JsonProperty("execution_id") public String execution_id;
    @JsonProperty("trigger_contexts") public  List<HassioContext> triggerContexts;
    @JsonProperty("action_contexts") public List<HassioContext> actionContexts;
 //   @JsonProperty("offset") public long offset;
    @JsonIgnore protected Trigger trigger;

    public HassioRuleExecutionEvent(Trigger trigger, Date datetime) {
        super(trigger.id, datetime);

        this.execution_id = UUID.randomUUID().toString();
        this.trigger = trigger;
        this.triggerContexts = new ArrayList<>();
        this.actionContexts = new ArrayList<>();
   //     this.offset = offset;
    }

    public HassioRuleExecutionEvent(String triggerID, Date datetime) {
        super(triggerID, datetime);

        this.execution_id = UUID.randomUUID().toString();
        this.trigger = null;
        this.triggerContexts = new ArrayList<>();
        this.actionContexts = new ArrayList<>();
    }

    public void addTriggerContexts(List<HassioContext> triggerContexts) {
        this.triggerContexts.addAll(triggerContexts);
    }

    public void addActionContexts(List<HassioContext> contexts) {
        this.actionContexts.addAll(contexts);
    }

    public Trigger getTrigger() {
        return this.trigger;
    }

    public void resolveContexts(HashMap<String, HassioState> hassioStates, List<String> triggerDevices, List<String> actionDevices) {
        for(String triggerDevice : triggerDevices) {
            this.triggerContexts.add(hassioStates.get(triggerDevice).context);
        }

        for(String actionDevice : actionDevices) {
            this.actionContexts.add(hassioStates.get(actionDevice).context);
        }
    }
}