package sven.phd.iot.rules.actions;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.outlet.HassioOutletAttributes;
import sven.phd.iot.hassio.sensor.HassioSensorAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateTempAction extends Action {
    private int offRate = -1;
    private int ecoRate = 1;
    private int heatingRate = 3;
    private int coolingRate = -3;
    private int rate;

    private String tempSensorID;

    public UpdateTempAction(String description, String tempSensorID, int rate) {
        super(description);

        this.tempSensorID = tempSensorID;
        this.rate = rate;
    }

    @Override
    public List<HassioState> simulate(HassioRuleExecutionEvent hassioRuleExecutionEvent, HashMap<String, HassioState> hassioStates) {
        float currentTemp = Float.parseFloat(hassioStates.get(tempSensorID).state);
        float newTemp = currentTemp + this.rate;

       /* HassioState heaterState = hassioStates.get("heater.heater");
        HassioState aircoState = hassioStates.get("airco.airco");
        float currentTemp = Float.parseFloat(hassioStates.get(tempSensorID).state);

        if(heaterState != null && heaterState.state.equals("eco")) {
            currentTemp += ecoRate;
        } else if(heaterState != null && heaterState.state.equals("heating")) {
            currentTemp += heatingRate;
        } else if(aircoState != null && aircoState.state.equals("cooling")) {
            currentTemp += coolingRate;
        } else {
            currentTemp += offRate;
        } */

        List<HassioState> newStates = new ArrayList<>();

        newStates.add(new HassioState(this.tempSensorID, "" + currentTemp, hassioRuleExecutionEvent.datetime, new HassioSensorAttributes("temperature", "°C")));

        return newStates;
    }
}
