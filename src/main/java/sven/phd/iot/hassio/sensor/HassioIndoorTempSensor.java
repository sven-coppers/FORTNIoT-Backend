package sven.phd.iot.hassio.sensor;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.thermostat.HassioThermostat;
import sven.phd.iot.hassio.thermostat.HassioThermostatAttributes;

import java.util.Date;
import java.util.HashMap;

public class HassioIndoorTempSensor extends HassioSensor {
    private double offRate = -2.0; // Degrees per hour
    private double ecoRate = 1.5; // Degrees per hour
    private double heatingRate = 12.0; // Degrees per hour
    private double coolingRate = -2.0; // Degrees per hour

    public HassioIndoorTempSensor(String entityID, String friendlyName) {
        super(entityID, friendlyName);
    }

    @Override
    protected HassioState adaptStateToContext(Date newDate, HashMap<String, HassioState> hassioStates) {
        Date oldDate = hassioStates.get(this.entityID).last_changed;
        Long deltaTimeInMilliseconds = newDate.getTime() - oldDate.getTime();
        double deltaTimeInHours = ((double) deltaTimeInMilliseconds) / (1000.0 * 60.0 * 60.0);

        HassioState heaterState = hassioStates.get("heater.heater");
        HassioState aircoState = hassioStates.get("airco.airco");
        double targetTemp = ((HassioThermostatAttributes) heaterState.attributes).targetTemp;
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
    }
}