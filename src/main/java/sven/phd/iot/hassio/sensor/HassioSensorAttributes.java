package sven.phd.iot.hassio.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioAttributes;

public class HassioSensorAttributes extends HassioAttributes {
    private String attribution;

    @JsonProperty("entity_picture") public String entityPicture;


    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }
}
