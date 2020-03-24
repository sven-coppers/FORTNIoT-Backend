package sven.phd.iot.hassio.states;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.light.HassioLightAttributes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HassioAttributes implements Serializable {
    @JsonProperty("friendly_name") public String friendlyName;

    public HassioAttributes() {
        // Empty constructor for serialisation
    }

    public HassioAttributes(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    /**
     * Override equals function
     * @param other
     * @return List of attributes that aren't equal
     *//*
    public List<HassioConflictingAttribute> checkForConflicts(HassioLightAttributes other) {
        List<HassioConflictingAttribute> result = new ArrayList<>();
        if (this.friendlyName != other.friendlyName) {
            result.add(new HassioConflictingAttribute("friendly_name", this.friendlyName, other.friendlyName));
        }

        return result;
    }*/
}