package sven.phd.iot.hassio.tracker;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioAttributes;

import java.util.Date;
@JsonIgnoreProperties(ignoreUnknown = true)
public class HassioDeviceTrackerAttributes extends HassioAttributes {
    @JsonProperty("source_type") String SourceType;

    public HassioDeviceTrackerAttributes() {
        // Used by the serializer
    }
}