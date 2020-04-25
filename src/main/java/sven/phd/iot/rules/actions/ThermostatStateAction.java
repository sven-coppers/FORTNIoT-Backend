package sven.phd.iot.rules.actions;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.climate.HassioThermostatAttributes;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ThermostatStateAction extends StateAction {
    private final double targetTemp;

    public ThermostatStateAction(String deviceIdentifier, String friendlyName, double targetTemp) {
        super("set " + friendlyName + " to " + targetTemp + "°C", deviceIdentifier, "" + targetTemp);
        this.targetTemp = targetTemp;
    }

    public List<HassioState> simulate(HassioRuleExecutionEvent hassioRuleExecutionEvent, HashMap<String, HassioState> hassioStates) {
        List<HassioState> newStates = new ArrayList<>();

        newStates.add(new HassioState(this.deviceIdentifier, newState, hassioRuleExecutionEvent.datetime, new HassioThermostatAttributes()));

        return newStates;
    }
    public String getDeviceIdentifier() {
        return this.deviceIdentifier;
    }
}
