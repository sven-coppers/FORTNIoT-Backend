package sven.phd.iot.hassio.climate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.sensor.HassioSensorAttributes;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.ImplicitBehaviorEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HassioHeater extends HassioTemperatureModifier {
    private final double onRate;
    private final double ecoRate;

    public HassioHeater(String entityID, String friendlyName, double onRate, double ecoRate, String thermostatID, String tempSensorID) {
        super(entityID, friendlyName, thermostatID, tempSensorID);
        this.onRate = onRate;
        this.ecoRate = ecoRate;
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioHeaterAttributes.class);
    }

    @Override
    protected List<ImplicitBehaviorEvent> predictFutureStatesUsingContext(Date newDate, HashMap<String, HassioState> hassioStates) {
        List<ImplicitBehaviorEvent> result = new ArrayList<>();
        HassioState thermostatState = hassioStates.get(this.thermostatID);
        HassioState temperatureState = hassioStates.get(this.tempSensorID);
        HassioState heaterState = hassioStates.get(this.entityID);

        if(thermostatState == null || temperatureState == null || heaterState == null) return result;

        double targetTemp = Double.parseDouble(thermostatState.state);
        double currentTemp = Double.parseDouble(temperatureState.state);
        Long deltaTimeInMilliseconds = newDate.getTime() - temperatureState.getLastChanged().getTime();
        double deltaTimeInHours = ((double) deltaTimeInMilliseconds) / (1000.0 * 60.0 * 60.0);

        // Adjust Temp
        if(heaterState.state.equals("heating")) {
            double newTemp = currentTemp + deltaTimeInHours * onRate;
            hassioStates.put(this.tempSensorID, new HassioState(this.tempSensorID, "" + newTemp, temperatureState.getLastChanged(), new HassioSensorAttributes("temperature", "°C")));
            temperatureState.state = "" + newTemp;

            // Stop heating?
            if(currentTemp > targetTemp) {
                hassioStates.put(this.entityID, new HassioState(this.entityID, "eco", heaterState.getLastChanged(), null));
                heaterState.state = "eco";

                ImplicitBehaviorEvent newState = new ImplicitBehaviorEvent(newDate);
                newState.addActionDeviceID(this.entityID);
                newState.addTriggerDeviceID(this.thermostatID);
                newState.addTriggerDeviceID(this.tempSensorID);
                result.add(newState);
            }
        } else if(heaterState.state.equals("eco")) {
            if(currentTemp > targetTemp) {
                // Do nothing
            } else {
                double newTemp = currentTemp + deltaTimeInHours * ecoRate;
                hassioStates.put(this.tempSensorID, new HassioState(this.tempSensorID, "" + newTemp, temperatureState.getLastChanged(), new HassioSensorAttributes("temperature", "°C")));
                temperatureState.state = "" + newTemp;

                // Start heating again?
                if(currentTemp < targetTemp - 1.0) {
                    hassioStates.put(this.entityID, new HassioState(this.entityID, "heating", heaterState.getLastChanged(), null));
                    heaterState.state = "heating";

                    ImplicitBehaviorEvent newState = new ImplicitBehaviorEvent(newDate);
                    newState.addActionDeviceID(this.entityID);
                    newState.addTriggerDeviceID(this.thermostatID);
                    newState.addTriggerDeviceID(this.tempSensorID);
                    result.add(newState);
                }
            }
        }

        return result;
    }
}
