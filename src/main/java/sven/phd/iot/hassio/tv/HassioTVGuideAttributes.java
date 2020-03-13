package sven.phd.iot.hassio.tv;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioAttributes;

public class HassioTVGuideAttributes extends HassioAttributes {
    @JsonProperty("content_type") public String contentType;

    public HassioTVGuideAttributes() {
        // Empty constructor for deserialization
    }

    public HassioTVGuideAttributes(String contentType) {
        this.contentType = contentType;
    }
}
