package sven.phd.iot.hassio.updates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Trigger;

import java.util.*;

public class RuleExecutionEvent extends ExecutionEvent {
    @JsonProperty("execution_id") public String execution_id;
    @JsonProperty("condition_satisfying_contexts") public HashMap<String, List<HassioContext>> conditionContexts;
    @JsonProperty("trigger_context") public List<HassioContext> triggerContexts;
    @JsonIgnore protected Trigger trigger;

    public RuleExecutionEvent(Trigger trigger, Date datetime, List<HassioContext> triggerContexts) {
        super(trigger.id, datetime);

        this.execution_id = UUID.randomUUID().toString();
        this.trigger = trigger;
        this.conditionContexts = new HashMap<>();
        this.triggerContexts = triggerContexts;
    }

    public RuleExecutionEvent(String triggerID, Date datetime) {
        super(triggerID, datetime);

        this.execution_id = UUID.randomUUID().toString();
        this.trigger = null;
        this.conditionContexts = new HashMap<>();
        this.triggerContexts = new ArrayList<>();
    }

    public void addConditionContext(String conditionID, HassioContext context) {
        if(this.conditionContexts.get(conditionID) == null) {
            this.conditionContexts.put(conditionID, new ArrayList<>());
        }

        this.conditionContexts.get(conditionID).add(context);
    }

    public Trigger getTrigger() {
        return this.trigger;
    }
}