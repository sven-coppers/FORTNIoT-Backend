package sven.phd.iot.hassio.change;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioState;

public class HassioChangeData {
    @JsonProperty("entity_id") public String entityId;
    @JsonProperty("old_state") public HassioState oldState;
    @JsonProperty("new_state") public HassioState newState;

    public HassioChangeData(String entityID, HassioState oldState, HassioState newState) {
        this.entityId = entityID;
        this.oldState = oldState;
        this.newState = newState;
    }
}
