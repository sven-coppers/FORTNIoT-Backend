package sven.phd.iot.rules.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class StateAction extends Action {
    @JsonProperty("deviceID") public String deviceIdentifier;
    @JsonProperty("new_state") public String newState;

    public StateAction() {
    }

    public StateAction(String description, String deviceIdentifier, String newState) {
        super(description);
        this.deviceIdentifier = deviceIdentifier;
        this.newState = newState;
    }

    public List<HassioState> simulate(Date datetime, HashMap<String, HassioState> hassioStates) {
        List<HassioState> newStates = new ArrayList<>();

        newStates.add(new HassioState(this.deviceIdentifier, newState, datetime, null));

        return newStates;
    }

    public String getDeviceID() {
        return this.deviceIdentifier;
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
            return (this.deviceIdentifier.equals(((StateAction) other).deviceIdentifier) && this.newState.equals(((StateAction) other).newState));
        }

        return false;
    }
}