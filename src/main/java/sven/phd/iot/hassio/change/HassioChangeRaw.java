package sven.phd.iot.hassio.change;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioContext;

import java.util.Date;

public class HassioChangeRaw {
    @JsonProperty("context") public HassioContext hassioContext;
    @JsonProperty("data") public HassioChangeDataRaw hassioChangeData;
    @JsonProperty("event_type") public String eventType;
    @JsonProperty("origin") public String origin;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonProperty("time_fired") public Date timeFired;
}