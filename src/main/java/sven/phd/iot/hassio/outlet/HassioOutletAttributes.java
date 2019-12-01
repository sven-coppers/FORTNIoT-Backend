package sven.phd.iot.hassio.outlet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioAttributes;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HassioOutletAttributes extends HassioAttributes {
    @JsonProperty("current_power_w") public Double power;
}
