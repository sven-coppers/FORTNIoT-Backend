package sven.phd.iot.hassio.change;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioStateRaw;

public class HassioChangeDataRaw {
    @JsonProperty("entity_id") public String entityId;
    @JsonProperty("new_state") public HassioStateRaw newState;
    @JsonProperty("old_state") public HassioStateRaw oldState;
}
