package sven.phd.iot.rules.actions;

import sven.phd.iot.hassio.outlet.HassioOutletAttributes;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OutletAction extends Action {
    @JsonProperty("entity_id")
    private final String deviceIdentifier;
    private final String newState;

    public OutletAction(String description, String deviceIdentifier, String newState) {
        super(description);
        this.deviceIdentifier = deviceIdentifier;
        this.newState = newState;
    }

    public List<HassioState> simulate(HassioRuleExecutionEvent hassioRuleExecutionEvent, HashMap<String, HassioState> hassioStates) {
        List<HassioState> newStates = new ArrayList<>();

        newStates.add(new HassioState(this.deviceIdentifier, newState, hassioRuleExecutionEvent.datetime, new HassioOutletAttributes()));

        return newStates;
    }
    public String getDeviceIdentifier() {
        return this.deviceIdentifier;
    }
}