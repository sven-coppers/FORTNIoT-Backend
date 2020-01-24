package sven.phd.iot.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigurationRequest {
    @JsonProperty("known") public boolean predictions;
    @JsonProperty("rules") public boolean updates;
}
