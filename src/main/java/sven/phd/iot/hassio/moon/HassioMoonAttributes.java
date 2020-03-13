package sven.phd.iot.hassio.moon;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioAttributes;

public class HassioMoonAttributes extends HassioAttributes {
    @JsonProperty("icon") String icon;


    public HassioMoonAttributes() {
        // Used by the serializer
    }
}