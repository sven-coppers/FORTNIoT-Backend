package sven.phd.iot.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UseCaseRequest {
    @JsonProperty("rule_set") public String ruleSet;
    @JsonProperty("device_set") public String deviceSet;
    @JsonProperty("state_set") public String stateSet;
}
