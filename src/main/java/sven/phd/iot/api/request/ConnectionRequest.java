package sven.phd.iot.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectionRequest {
    @JsonProperty("connected_to_hassio") public boolean connectedToHassio;
}
