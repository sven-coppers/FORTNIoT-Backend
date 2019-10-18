package sven.phd.iot.hassio.services;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioContext;

import java.util.Date;

public class HassioCallService {
    @JsonProperty("context") public HassioContext hassioContext;
    @JsonProperty("data") public HassioCallServiceData hassioCallServiceData;
    @JsonProperty("event_type") public String eventType;
    @JsonProperty("origin") public String origin;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSX")
    @JsonProperty("time_fired") public Date timeFired;
}