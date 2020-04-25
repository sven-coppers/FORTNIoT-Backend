package sven.phd.iot.rules.actions;

import sven.phd.iot.hassio.light.HassioLightAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LightOffAction extends Action {
    private final String deviceIdentifier;

    public LightOffAction(String description, String deviceIdentifier) {
        super(description);
        this.deviceIdentifier = deviceIdentifier;
    }

    public List<HassioState> simulate(HassioRuleExecutionEvent hassioRuleExecutionEvent, HashMap<String, HassioState> hassioStates) {
        List<HassioState> newStates = new ArrayList<>();

        newStates.add(new HassioState(this.deviceIdentifier, "off", hassioRuleExecutionEvent.datetime, new HassioLightAttributes()));

        return newStates;
    }
}