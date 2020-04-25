package sven.phd.iot.students.bram.questions.why_not;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrentState {
    @JsonProperty("value")
    public String currentState;
    @JsonProperty("device_id")
    public String deviceId;
    @JsonProperty("device_name")
    public String deviceName;
}
