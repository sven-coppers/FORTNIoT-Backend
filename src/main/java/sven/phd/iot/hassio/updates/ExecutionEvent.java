package sven.phd.iot.hassio.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;
import sven.phd.iot.hassio.states.HassioState;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ExecutionEvent implements Comparable<ExecutionEvent> {
    @JsonProperty("entity_id") public String entity_id;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("datetime") public Date datetime;
    @JsonProperty("action_contexts") public HashMap<String, List<HassioContext>> actionContexts;

    public ExecutionEvent(String entityID, Date datetime) {
        this.entity_id = entityID;
        this.datetime = datetime;
        this.actionContexts = new HashMap<>();
    }

    public void addActionExecuted(String actionID, HassioState state) {
        if(this.actionContexts.get(actionID) == null) {
            this.actionContexts.put(actionID, new ArrayList<>());
        }

        this.actionContexts.get(actionID).add(state.context);
    }

    public void addActionExecuted(String actionID, List<HassioState> states) {
        if(this.actionContexts.get(actionID) == null) {
            this.actionContexts.put(actionID, new ArrayList<>());
        }

        for(HassioState hassioState : states) {
            this.actionContexts.get(actionID).add(hassioState.context);
        }
    }

    public String getResponsibleAction(HassioContext findContext) {
        for(String actionID : this.actionContexts.keySet()) {
            for(HassioContext haystackContext : this.actionContexts.get(actionID)) {
                if(haystackContext.id.equals(findContext.id)) {
                    return actionID;
                }
            }
        }

        return null;
    }

    public int compareTo(ExecutionEvent hassioEvent) {
        if(hassioEvent.datetime == null || this.datetime == null) {
            System.out.println();
        }
        return this.datetime.compareTo(hassioEvent.datetime);
    }
}
