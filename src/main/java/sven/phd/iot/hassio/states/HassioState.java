package sven.phd.iot.hassio.states;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class HassioState extends HassioAbstractState {
    public HassioAttributes attributes;
    @JsonProperty("caused_by_event") private String executionEvent;

    public HassioState(String entityID, String state, Date date, HassioAttributes attributes) {
        super(new HassioContext(entityID, date), entityID, date, date, state);

        this.attributes = attributes;
    }

    public HassioState(HassioStateRaw hassioStateRaw, HassioAttributes attributes) {
        super(hassioStateRaw);

        this.attributes = attributes;
    }

    // Copy Constructor with a new date
    public HassioState(HassioState hassioState, Date date) {
        super(new HassioContext(hassioState.entity_id, date), hassioState.entity_id, date, date, hassioState.state);
        this.attributes = hassioState.attributes;
    }

    public String getExecutionEvent() {
        return executionEvent;
    }

    public void setExecutionEvent(String executionEvent) {
        this.executionEvent = executionEvent;
    }

   /* public Conflict compareAttributes(HassioState state) {
        /*
        if(this.state != state.state) {
            HassioConflictState conflict = new HassioConflictState(this.entity_id, this.type, this.datetime);
            HassioConflictingAttribute attr = new HassioConflictingAttribute("state", this.state, state.state);
            conflict.conflicts.add(attr);
            return conflict;
        }

        return null;
    }*/
}
