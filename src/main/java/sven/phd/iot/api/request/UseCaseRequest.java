package sven.phd.iot.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UseCaseRequest {
    @JsonProperty("rule_set") public List<String> ruleSet;
    @JsonProperty("device_set") public List<String> deviceSet;
    @JsonProperty("state_set") public List<String> stateSet;
    @JsonProperty("preset") public String preset;
    @JsonProperty("rule_set_options") public List<String> ruleSetOptions;
    @JsonProperty("device_set_options") public List<String> deviceSetOptions;
    @JsonProperty("state_set_options") public List<String> stateSetOptions;
    @JsonProperty("preset_options") public List<String> presetOptions;
}
