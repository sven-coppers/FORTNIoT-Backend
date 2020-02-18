package sven.phd.iot.hassio.sensor;

import sven.phd.iot.hassio.states.HassioState;

import java.util.Date;
import java.util.HashMap;

public class HassioIndoorTempSensor extends HassioSensor {
    private int offRate = -1;
    private int ecoRate = 1;
    private int heatingRate = 3;
    private int coolingRate = -3;

    public HassioIndoorTempSensor(String entityID, String friendlyName) {
        super(entityID, friendlyName);
    }

    @Override
    protected HassioState adaptStateToContext(Date newDate, HashMap<String, HassioState> hassioStates) {
        HassioState heaterState = hassioStates.get("heater.heater");
        HassioState aircoState = hassioStates.get("airco.airco");
        float currentTemp = Float.parseFloat(hassioStates.get(this.entityID).state);

        if(heaterState != null && heaterState.state.equals("eco")) {
            currentTemp += ecoRate;
        } else if(heaterState != null && heaterState.state.equals("heating")) {
            currentTemp += heatingRate;
        } else if(aircoState != null && aircoState.state.equals("cooling")) {
            currentTemp += coolingRate;
        } else {
            currentTemp += offRate;
        }

        return new HassioState(this.entityID, "" + currentTemp, newDate, new HassioSensorAttributes("temperature", "°C"));
    }
}
