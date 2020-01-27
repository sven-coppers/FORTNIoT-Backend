package sven.phd.iot.hassio.sensor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioAttributes;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HassioBinarySensorAttributes extends HassioAttributes {
    @JsonProperty("device_class") public String deviceClass;
    @JsonProperty("restored") public boolean restored;
    @JsonProperty("supported_features") public int supported_features;
}
