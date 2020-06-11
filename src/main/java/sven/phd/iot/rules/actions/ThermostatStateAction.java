package sven.phd.iot.rules.actions;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.climate.HassioThermostatAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ThermostatStateAction extends StateAction {
    private final double targetTemp;

    public ThermostatStateAction(String deviceIdentifier, String friendlyName, double targetTemp) {
        super("set " + friendlyName + " to " + targetTemp + "°C", deviceIdentifier, "" + targetTemp);
        this.targetTemp = targetTemp;
    }

    public List<HassioState> simulate(Date datetime, HashMap<String, HassioState> hassioStates) {
        List<HassioState> newStates = new ArrayList<>();

        newStates.add(new HassioState(this.deviceIdentifier, newState, datetime, new HassioThermostatAttributes()));

        return newStates;
    }
}
