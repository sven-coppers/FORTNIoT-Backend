package sven.phd.iot.students.bram.questions.why;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WhyResult {
    @JsonProperty("actor")
    public String actor;
    @JsonProperty("state")
    public String state;
    @JsonProperty("friendly_name")
    public String friendly_name;
    @JsonProperty("device_id")
    public String device_id;
}
