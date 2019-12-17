package sven.phd.iot.hassio.states;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.light.HassioLightAttributes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HassioAttributes implements Serializable {
    @JsonProperty("friendly_name") public String friendly_name;

    /**
     * Override equals function
     * @param other
     * @return List of attributes that aren't equal
     */
    public List<HassioConflictingAttribute> checkForConflicts(HassioLightAttributes other) {
        List<HassioConflictingAttribute> result = new ArrayList<>();
        if (this.friendly_name != other.friendly_name) {
            result.add(new HassioConflictingAttribute("friendly_name", this.friendly_name, other.friendly_name));
        }

        return result;
    }
}