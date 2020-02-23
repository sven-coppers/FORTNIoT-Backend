package sven.phd.iot.students.bram.rules.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.light.HassioLightState;
import sven.phd.iot.hassio.outlet.HassioOutletState;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.List;

public class OutletOffAction extends Action {
    @JsonProperty("entity_id")
    private final String deviceIdentifier;

    public OutletOffAction(String deviceIdentifier) {
        super("Turn off " + deviceIdentifier);
        this.deviceIdentifier = deviceIdentifier;
    }

    public List<HassioState> simulate(HassioRuleExecutionEvent hassioRuleExecutionEvent) {
        List<HassioState> newStates = new ArrayList<>();

        HassioOutletState hassioOutletState = new HassioOutletState(deviceIdentifier, "off", hassioRuleExecutionEvent.datetime);

        newStates.add(hassioOutletState);

        return newStates;
    }
}
