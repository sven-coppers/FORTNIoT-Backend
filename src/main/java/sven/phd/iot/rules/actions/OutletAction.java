package sven.phd.iot.rules.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.light.HassioLightState;
import sven.phd.iot.hassio.outlet.HassioOutletState;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.List;

public class OutletAction extends Action {
    @JsonProperty("entity_id")
    private final String deviceIdentifier;
    private final String newState;

    public OutletAction(String deviceIdentifier, String newState) {
        super("Set " + deviceIdentifier + " to " + newState);
        this.deviceIdentifier = deviceIdentifier;
        this.newState = newState;
    }

    public List<HassioState> simulate(HassioRuleExecutionEvent hassioRuleExecutionEvent) {
        List<HassioState> newStates = new ArrayList<>();

        HassioOutletState hassioOutletState = new HassioOutletState(this.deviceIdentifier, newState, hassioRuleExecutionEvent.datetime);

        newStates.add(hassioOutletState);

        return newStates;
    }
}