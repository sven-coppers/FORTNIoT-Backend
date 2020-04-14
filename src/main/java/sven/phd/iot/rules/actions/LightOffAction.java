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

        if (!isEnabled(hassioRuleExecutionEvent.datetime)) {
            return newStates;
        }

        newStates.add(new HassioState(this.deviceIdentifier, "off", hassioRuleExecutionEvent.datetime, new HassioLightAttributes()));

        return newStates;
    }

    public String getDeviceID() { return this.deviceIdentifier; }

    @Override
    public boolean onSameDevice(Action other) {
        if (this == other) {
            return true;
        }
        if (other == null || (this.getClass() != other.getClass() && LightOnAction.class != other.getClass())) {
            return false;
        }
        if (this.getClass() == other.getClass()) {
            return this.deviceIdentifier.equals(((LightOffAction) other).deviceIdentifier);
        }
        if (LightOnAction.class == other.getClass()) {
            return this.deviceIdentifier.equals(((LightOnAction) other).getDeviceID());
        }

        return false;
    }
}