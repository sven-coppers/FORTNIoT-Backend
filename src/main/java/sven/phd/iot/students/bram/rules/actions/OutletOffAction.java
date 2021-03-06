package sven.phd.iot.students.bram.rules.actions;

import sven.phd.iot.hassio.outlet.HassioOutletAttributes;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OutletOffAction extends Action {
    @JsonProperty("entity_id")
    private final String deviceIdentifier;

    public OutletOffAction(String deviceIdentifier) {
        super("Turn off " + deviceIdentifier);
        this.deviceIdentifier = deviceIdentifier;
    }

    public List<HassioState> simulate(Date datetime, HashMap<String, HassioState> hassioStates) {
        List<HassioState> newStates = new ArrayList<>();
        
        newStates.add(new HassioState(deviceIdentifier, "off", datetime, new HassioOutletAttributes()));

        return newStates;
    }
    public String getDeviceIdentifier() {
        return this.deviceIdentifier;
    }
}
