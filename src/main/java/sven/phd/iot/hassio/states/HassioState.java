package sven.phd.iot.hassio.states;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.updates.HassioUpdate;

import java.util.Date;

abstract public class HassioState extends HassioUpdate {
    @JsonProperty("context") public HassioContext context;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    public Date last_changed;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    private Date last_updated;

    @JsonSetter("last_updated")
    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
        this.datetime = last_updated;
    }

    @JsonGetter("last_updated")
    public Date getLast_updated() {
        return this.last_updated;
    }

    @JsonProperty("state") public String state;

    public HassioState(HassioState hassioState) {
        super(hassioState.entity_id, "HassioState", hassioState.last_changed);

        if(hassioState.context == null) {
            throw new NullPointerException();
        }

        this.context = hassioState.context;
        this.entity_id = hassioState.entity_id;
        this.last_changed = hassioState.last_changed;
        this.last_updated = hassioState.last_updated;
        this.state = hassioState.state;
    }

    public HassioState(HassioContext context, String entity_id, Date last_changed, Date last_updated, String state) {
        super(entity_id, "HassioState", last_updated);

        if(context == null) {
            throw new NullPointerException();
        }

        this.context = context;
        this.entity_id = entity_id;
        this.last_changed = last_changed;
        this.last_updated = last_updated;
        this.state = state;
    }

    // Default constructor for serialization purposes
    public HassioState() {
        this.context = new HassioContext();
    }

    public HassioConflictState compareAttributes(HassioState state) {
        if(this.state != state.state) {
            HassioConflictState conflict = new HassioConflictState(this.entity_id, this.type, this.datetime);
            HassioConflictingAttribute attr = new HassioConflictingAttribute("state", this.state, state.state);
            conflict.conflicts.add(attr);
            return conflict;
        }

        return null;
    }
}
