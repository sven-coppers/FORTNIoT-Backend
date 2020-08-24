package sven.phd.iot.rules.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.light.HassioLightAttributes;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class LightOffAction extends Action {
    @JsonProperty("deviceID") public String deviceIdentifier;

    // For deserialization
    public LightOffAction() {
        enabled = true;
    }

    public LightOffAction(String description, String deviceIdentifier) {
        super(description);
        this.deviceIdentifier = deviceIdentifier;
    }

    public List<HassioState> simulate(Date datetime, HashMap<String, HassioState> hassioStates) {
        List<HassioState> newStates = new ArrayList<>();

        if (!isEnabled(datetime)) {
            return newStates;
        }

        newStates.add(new HassioState(this.deviceIdentifier, "off", datetime, new HassioLightAttributes()));

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

    @Override
    public boolean isSimilar(Action other) {
        if (this == other) {
            return true;
        }
        if (other == null || (this.getClass() != other.getClass() && LightOnAction.class != other.getClass())) {
            return false;
        }
        if (this.getClass() == other.getClass()) {
            return this.deviceIdentifier.equals(((LightOffAction) other).deviceIdentifier);
        }

        return false;
    }
}