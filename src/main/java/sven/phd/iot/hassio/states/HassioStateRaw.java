package sven.phd.iot.hassio.states;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import sven.phd.iot.hassio.states.HassioState;

public class HassioStateRaw extends HassioState {
    @JsonProperty("attributes") public JsonNode attributes;

    @Override
    public HassioState processRawState(HassioStateRaw raw) {
        return null;
    }
}
