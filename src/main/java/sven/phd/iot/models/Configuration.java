package sven.phd.iot.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Configuration {
    @JsonProperty("listing_to_hassio") public boolean listeningToHassio;
}
