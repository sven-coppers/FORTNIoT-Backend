package sven.phd.iot.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.overrides.SnoozedAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimulationRequest {
    @JsonProperty("extra_states") public List<HassioState> extraStates;
    @JsonProperty("suppressed_state_contexts") public List<HassioContext> suppressedStateContexts;

    @JsonProperty("snoozed_actions") public List<SnoozedAction> snoozedActions;
    @JsonProperty("re_enabled_actions") public List<String> reEnabledActions;

    public SimulationRequest() {
        extraStates = new ArrayList<>();
        suppressedStateContexts = new ArrayList<>();
        snoozedActions = new ArrayList<>();
        reEnabledActions = new ArrayList<>();
    }
}
