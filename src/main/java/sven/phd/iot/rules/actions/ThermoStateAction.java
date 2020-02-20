package sven.phd.iot.rules.actions;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.thermostat.HassioThermostatAttributes;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ThermoStateAction extends StateAction {
    private final double targetTemp;

    public ThermoStateAction(String deviceIdentifier, String newState, double targetTemp) {
        super("set thermostat to " + targetTemp + "°C", deviceIdentifier, newState);
        this.targetTemp = targetTemp;
    }

    public List<HassioState> simulate(HassioRuleExecutionEvent hassioRuleExecutionEvent, HashMap<String, HassioState> hassioStates) {
        List<HassioState> newStates = new ArrayList<>();

        newStates.add(new HassioState(this.deviceIdentifier, newState, hassioRuleExecutionEvent.datetime, new HassioThermostatAttributes(this.targetTemp)));

        return newStates;
    }
}
