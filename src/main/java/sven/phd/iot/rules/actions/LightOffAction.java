package sven.phd.iot.rules.actions;

import sven.phd.iot.hassio.light.HassioLightState;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.List;

public class LightOffAction extends Action {
    private final String deviceIdentifier;

    public LightOffAction(String deviceIdentifier) {
        super("Turn off " + deviceIdentifier);
        this.deviceIdentifier = deviceIdentifier;
    }

    public List<HassioState> simulate(HassioRuleExecutionEvent hassioRuleExecutionEvent) {
        List<HassioState> newStates = new ArrayList<>();

        HassioLightState hassioLightState = new HassioLightState(this.deviceIdentifier, "off", hassioRuleExecutionEvent.datetime);
        hassioLightState.attributes.brightness = 0;

        newStates.add(hassioLightState);

        return newStates;
    }
}