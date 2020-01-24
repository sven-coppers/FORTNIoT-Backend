package sven.phd.iot.hassio.thermostat;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioAttributes;

public class HassioThermostatAttributes extends HassioAttributes {
    @JsonProperty("target_temp") public float targetTemp;
}
