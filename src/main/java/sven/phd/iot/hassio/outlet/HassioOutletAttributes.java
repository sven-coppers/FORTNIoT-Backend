package sven.phd.iot.hassio.outlet;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioAttributes;

public class HassioOutletAttributes extends HassioAttributes {
    @JsonProperty("current_power_w") public Double power;
}
