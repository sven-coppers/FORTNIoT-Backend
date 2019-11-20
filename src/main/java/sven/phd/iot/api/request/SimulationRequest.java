package sven.phd.iot.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioState;

import java.util.HashMap;
import java.util.List;

public class SimulationRequest {
    @JsonProperty("states") public List<HassioState> hassioStates;
    @JsonProperty("rules") public HashMap<String, Boolean> enabledRules;
}
