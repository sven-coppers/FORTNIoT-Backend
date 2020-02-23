package sven.phd.iot.hassio.routine;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioAttributes;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HassioRoutineAttributes extends HassioAttributes {
    // Empty for now
}
