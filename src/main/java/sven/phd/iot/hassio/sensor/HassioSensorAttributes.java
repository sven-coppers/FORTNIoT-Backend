package sven.phd.iot.hassio.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioAttributes;

public class HassioSensorAttributes extends HassioAttributes {
    @JsonProperty("device_class") public String deviceClass;
    @JsonProperty("unit_of_measurement") public String unitOfMeasurement;
}
