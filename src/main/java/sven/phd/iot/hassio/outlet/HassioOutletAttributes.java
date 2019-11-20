package sven.phd.iot.hassio.outlet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import sven.phd.iot.hassio.states.HassioAttributes;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HassioOutletAttributes extends HassioAttributes {
    // No additional properties

}
