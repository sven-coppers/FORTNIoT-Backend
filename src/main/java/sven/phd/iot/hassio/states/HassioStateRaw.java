package sven.phd.iot.hassio.states;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class HassioStateRaw extends HassioAbstractState {
    @JsonProperty("attributes") public JsonNode attributes;
}
