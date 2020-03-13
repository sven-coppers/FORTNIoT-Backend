package sven.phd.iot.hassio.change;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.states.*;
import sven.phd.iot.hassio.updates.HassioUpdate;

import java.util.Date;

public class HassioChange extends HassioUpdate {
    @JsonProperty("context") public HassioContext hassioContext;
    @JsonProperty("data") public HassioChangeData hassioChangeData;
    @JsonProperty("event_type") public String eventType;
    @JsonProperty("origin") public String origin;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("time_fired") public Date triggerTime;

    public void setTime_fired(Date triggerTime) {
        this.triggerTime = triggerTime;
        this.datetime = triggerTime;
    }

    public HassioChange(String entityID, HassioState oldState, HassioState newState, Date triggerTime) {
        super(entityID, "HassioChange", triggerTime);

        this.hassioContext = null;
        this.hassioChangeData = new HassioChangeData(entityID, oldState, newState);
        this.eventType = null;
        this.origin = "SIMULATION";
        this.triggerTime = triggerTime;
        this.hassioContext = newState.context;
    }
}