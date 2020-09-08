package sven.phd.iot.hassio.climate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.sensor.HassioSensorAttributes;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.ImplicitBehaviorEvent;
import java.io.IOException;
import java.util.*;

public class HassioCooler extends HassioTemperatureModifier {
    private final double onRate;
    private final double ecoRate;

    public HassioCooler(String entityID, String friendlyName, double onRate, double ecoRate, String thermostatID, String tempSensorID) {
        super(entityID, friendlyName, thermostatID, tempSensorID);
        this.onRate = onRate;
        this.ecoRate = ecoRate;
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioHeaterAttributes.class);
    }

    @Override
    protected List<ImplicitBehaviorEvent> predictImplicitStates(Date newDate, HashMap<String, HassioState> hassioStates, Map<String, HassioDevice> hassioDeviceMap) {
        List<ImplicitBehaviorEvent> result = new ArrayList<>();
        HassioState thermostatState = hassioStates.get(this.thermostatID);
        HassioState temperatureState = hassioStates.get(this.tempSensorID);
        HassioState heaterState = hassioStates.get(this.entityID);

        if(thermostatState == null || temperatureState == null || heaterState == null) return result;

        double targetTemp = Double.parseDouble(thermostatState.state);
        double currentTemp = Double.parseDouble(temperatureState.state);
        Long deltaTimeInMilliseconds = newDate.getTime() - temperatureState.getLastChanged().getTime();
        double deltaTimeInHours = ((double) deltaTimeInMilliseconds) / (1000.0 * 60.0 * 60.0);

        // Give the new state the old date, because it might be changed by another device as well

        // Adjust Temp
        if(heaterState.state.equals("on")) {
            double newTemp = currentTemp + deltaTimeInHours * onRate;
            hassioStates.put(this.tempSensorID, new HassioState(this.tempSensorID, "" + newTemp, temperatureState.getLastChanged(), new HassioSensorAttributes("temperature", "°C")));
            result.add(new ImplicitBehaviorEvent(newDate));

            // Stop heating?
            if(currentTemp < targetTemp) {
                hassioStates.put(this.entityID, new HassioState(this.entityID, "eco", heaterState.getLastChanged(), new HassioHeaterAttributes()));
                result.add(new ImplicitBehaviorEvent(newDate));

                ImplicitBehaviorEvent newBehavior = new ImplicitBehaviorEvent( newDate);
                newBehavior.addActionDeviceID(this.entityID);
                newBehavior.addTriggerDeviceID(this.thermostatID);
                newBehavior.addTriggerDeviceID(this.tempSensorID);
                result.add(newBehavior);
            }
        } else if(heaterState.state.equals("eco")) {
            if(currentTemp > targetTemp) {
                // Do nothing
            } else {
                double newTemp = currentTemp + deltaTimeInHours * ecoRate;
                hassioStates.put(this.tempSensorID, new HassioState(this.tempSensorID, "" + newTemp, temperatureState.getLastChanged(), new HassioSensorAttributes("temperature", "°C")));
                result.add(new ImplicitBehaviorEvent(newDate));

                // Start heating again?
                if(currentTemp > targetTemp + 1.0) {
                    hassioStates.put(this.entityID, new HassioState(this.entityID, "on", heaterState.getLastChanged(), new HassioHeaterAttributes()));
                    result.add(new ImplicitBehaviorEvent(newDate));

                    ImplicitBehaviorEvent newBehavior = new ImplicitBehaviorEvent(newDate);
                    newBehavior.addActionDeviceID(this.entityID);
                    newBehavior.addTriggerDeviceID(this.thermostatID);
                    newBehavior.addTriggerDeviceID(this.tempSensorID);
                    result.add(newBehavior);
                }
            }
        }

        return result;
    }

    public double getOnRate() {
        return onRate;
    }

    public double getEcoRate() {
        return ecoRate;
    }
}
