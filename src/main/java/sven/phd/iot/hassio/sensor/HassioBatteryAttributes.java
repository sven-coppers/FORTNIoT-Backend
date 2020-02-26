package sven.phd.iot.hassio.sensor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HassioBatteryAttributes extends HassioSensorAttributes {
    @JsonProperty("charger_type") public String chargerType;
    @JsonProperty("icon") public String icon;
    @JsonProperty("is_charging") public Boolean isCharging;
}
