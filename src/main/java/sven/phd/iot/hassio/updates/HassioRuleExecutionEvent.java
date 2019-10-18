package sven.phd.iot.hassio.updates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class HassioRuleExecutionEvent extends HassioEvent {
    @JsonProperty("execution_id") public String execution_id;
    @JsonProperty("trigger_contexts") public  List<HassioContext> triggerContexts;
    @JsonProperty("action_contexts") public List<HassioContext> actionContexts;
    @JsonProperty("offset") public long offset;
    @JsonIgnore private Trigger trigger;

    public HassioRuleExecutionEvent(Trigger trigger, Date datetime, long offset) {
        super(trigger.id, datetime);

        this.execution_id = UUID.randomUUID().toString();
        this.trigger = trigger;
        this.triggerContexts = new ArrayList<>();
        this.actionContexts = new ArrayList<>();
        this.offset = offset;
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
}