package sven.phd.iot.students.mathias.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;

import java.util.Date;
import java.util.List;

public class SolutionRequest {
    @JsonProperty("description") public String description;

    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("start_time") public Date startTime;

    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("stop_time") public Date stopTime;

    public SolutionRequest(){}

    public SolutionRequest(String description, Date startTime, Date stopTime) {
        this.description = description;
        this.startTime = startTime;
        this.stopTime = stopTime;
    }

    public SolutionRequest(SolutionRequest other) {
        this.description = other.description;
        this.startTime = other.startTime;
        this.stopTime = other.stopTime;
    }
}
