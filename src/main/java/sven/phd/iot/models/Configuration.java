package sven.phd.iot.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Configuration {
    @JsonProperty("connected_to_hassio") public boolean connectedToHassio;
}
