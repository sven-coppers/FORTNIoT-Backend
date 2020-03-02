package sven.phd.iot.hassio.sensor;

import sven.phd.iot.hassio.states.HassioState;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HassioIndoorTempSensor extends HassioSensor {
    private double coolingRate = -0.5; // Degrees per hour

    public HassioIndoorTempSensor(String entityID, String friendlyName, double coolingRate) {
        super(entityID, friendlyName);

        this.coolingRate = coolingRate;
    }

    @Override
    protected List<HassioState> adaptStateToContext(Date newDate, HashMap<String, HassioState> hassioStates) {
        List<HassioState> result = new ArrayList<>();

        Date oldDate = hassioStates.get(this.entityID).getLastChanged();
        Long deltaTimeInMilliseconds = newDate.getTime() - oldDate.getTime();
        double deltaTimeInHours = ((double) deltaTimeInMilliseconds) / (1000.0 * 60.0 * 60.0);
        double currentTemp = Double.parseDouble((hassioStates.get(this.entityID).state));
        double newTemp = currentTemp + coolingRate * deltaTimeInHours;

        // Give the new state the old date, because it might be changed by another device as well
        result.add(new HassioState(entityID, "" + newTemp, oldDate, new HassioSensorAttributes("temperature", "°C")));


   /*     HassioState heaterState = hassioStates.get("heater.heater");
        HassioState aircoState = hassioStates.get("airco.airco");
        double targetTemp = ((HassioHeaterAttributes) heaterState.attributes).targetTemp;
        double currentTemp = Double.parseDouble((hassioStates.get(this.entityID).state));

        if(heaterState != null && heaterState.state.equals("eco")) {
            double deltaTemp = Math.abs(ecoRate * deltaTimeInHours);

            if(currentTemp - deltaTemp > targetTemp) {
                currentTemp -= deltaTemp;
            } else {
                currentTemp = targetTemp;
            }
        } else if(heaterState != null && heaterState.state.equals("heating")) {
            currentTemp += heatingRate * deltaTimeInHours;
        } else if(aircoState != null && aircoState.state.equals("cooling")) {
            currentTemp += coolingRate * deltaTimeInHours;
        } else {
            currentTemp += offRate * deltaTimeInHours;
        }

        return new HassioState(this.entityID, "" + currentTemp, newDate, new HassioSensorAttributes("temperature", "°C"));
    */
        //result.add(this.entityID);
        return result;
    }
}