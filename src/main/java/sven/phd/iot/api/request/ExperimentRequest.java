package sven.phd.iot.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExperimentRequest {
    @JsonProperty("participant") public String participant;
    @JsonProperty("group") public String group;
}
