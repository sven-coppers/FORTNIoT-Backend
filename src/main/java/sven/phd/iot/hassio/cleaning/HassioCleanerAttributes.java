package sven.phd.iot.hassio.cleaning;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioAttributes;

public class HassioCleanerAttributes extends HassioAttributes {
    @JsonProperty("time_left") public double timeLeft;

    public HassioCleanerAttributes() {
        // Empty constructor for deserialization
    }

    public HassioCleanerAttributes(double timeLeft) {
        this.timeLeft = timeLeft;
    }
}