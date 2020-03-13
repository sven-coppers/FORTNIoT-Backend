package sven.phd.iot.hassio.climate;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioAttributes;

public class HassioCoolerAttributes extends HassioAttributes {
    @JsonProperty("cooling_rate") public double coolingRate;

    public HassioCoolerAttributes(double coolingRate) {
        this.coolingRate = coolingRate;
    }
}
