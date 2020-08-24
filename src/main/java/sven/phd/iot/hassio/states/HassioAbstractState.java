package sven.phd.iot.hassio.states;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

abstract public class HassioAbstractState implements Comparable<HassioAbstractState> {
    @JsonProperty("entity_id") public String entity_id;
    @JsonProperty("context") public HassioContext context;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    protected Date last_changed;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    protected Date last_updated;

    @JsonSetter("last_updated")
    public void setLastUpdated(Date last_updated) {
        this.last_updated = last_updated;
        this.context = new HassioContext(this.entity_id, last_updated);
    }

    @JsonGetter("last_updated")
    public Date getLastUpdated() {
        return this.last_updated;
    }

    @JsonSetter("last_changed")
    public void setLastChanged(Date lastChanged) {
        this.last_changed = lastChanged;
        this.context = new HassioContext(this.entity_id, lastChanged);
    }

    @JsonGetter("last_changed")
    public Date getLastChanged() {
        return this.last_changed;
    }

    @JsonProperty("state") public String state;

    // Default constructor for deserialization purposes
    public HassioAbstractState() {
        this.context = new HassioContext();
    }

    /**
     * Copy constructor
     * @param hassioAbstractState usually a HassioRawState
     */
    public HassioAbstractState(HassioAbstractState hassioAbstractState) {
        if(hassioAbstractState.context == null) {
            throw new NullPointerException();
        }

        this.context = hassioAbstractState.context;
        this.entity_id = hassioAbstractState.entity_id;
        this.last_changed = hassioAbstractState.last_changed;
        this.last_updated = hassioAbstractState.last_updated;
        this.state = hassioAbstractState.state;
    }

    public HassioAbstractState(HassioContext context, String entity_id, Date last_changed, Date last_updated, String state) {
        if(context == null) {
            throw new NullPointerException();
        }

        this.context = context;
        this.entity_id = entity_id;
        this.last_changed = last_changed;
        this.last_updated = last_updated;
        this.state = state;
    }

    public boolean isSimilar(HassioAbstractState hassioAbstractState) {
        boolean result = true;
        if (!this.entity_id.equals(hassioAbstractState.entity_id))
            result = false;
        if (!this.state.equals(hassioAbstractState.state))
            result = false;
        if (this.last_updated.compareTo(hassioAbstractState.last_updated) != 0)
            result = false;
        if (this.last_changed.compareTo(hassioAbstractState.last_changed) != 0)
            result = false;

        return result;
    }

    public int compareTo(HassioAbstractState hassioAbstractState) {
        if(hassioAbstractState.last_updated == null || this.last_updated == null) {
            System.out.println();
        }
        return this.last_updated.compareTo(hassioAbstractState.last_updated);
    }
}
