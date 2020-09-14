package sven.phd.iot.hassio.change;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.states.*;

import java.util.Date;

public class HassioChange implements Comparable<HassioChange> {
    @JsonProperty("entity_id") public String entity_id;

    @JsonProperty("context") public HassioContext hassioContext;
    @JsonProperty("data") public HassioChangeData hassioChangeData;
    @JsonProperty("event_type") public String eventType;
    @JsonProperty("origin") public String origin;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("time_fired") public Date triggerTime;

    public void setTime_fired(Date triggerTime) {
        this.triggerTime = triggerTime;
    }


    public HassioChange() {
        // Default constructor for deserialization
    }

    public HassioChange(String entityID, HassioState oldState, HassioState newState, Date triggerTime) {
        this.entity_id = entityID;
        this.hassioContext = null;
        this.hassioChangeData = new HassioChangeData(entityID, oldState, newState);
        this.eventType = null;
        this.origin = "SIMULATION";
        this.triggerTime = triggerTime;
        this.hassioContext = newState.context;
    }

    public int compareTo(HassioChange hassioChange) {
        if(hassioChange.triggerTime == null || this.triggerTime == null) {
            return -1;
        }

        return this.triggerTime.compareTo(hassioChange.triggerTime);
    }
}