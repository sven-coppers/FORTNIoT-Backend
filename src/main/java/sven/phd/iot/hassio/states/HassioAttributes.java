package sven.phd.iot.hassio.states;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class HassioAttributes implements Serializable {
    @JsonProperty("friendly_name") public String friendlyName;

    public HassioAttributes() {
        // Empty constructor for serialisation
    }

    public HassioAttributes(String friendlyName) {
        this.friendlyName = friendlyName;
    }
}