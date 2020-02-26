package sven.phd.iot.hassio.climate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.sensor.HassioSensorAttributes;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    protected List<HassioState> adaptStateToContext(Date newDate, HashMap<String, HassioState> hassioStates) {
        List<HassioState> result = new ArrayList<>();
        HassioState thermostatState = hassioStates.get(this.thermostatID);
        HassioState temperatureState = hassioStates.get(this.tempSensorID);
        HassioState heaterState = hassioStates.get(this.entityID);

        if(thermostatState == null || temperatureState == null || heaterState == null) return result;

        double targetTemp = Double.parseDouble(thermostatState.state);
        double currentTemp = Double.parseDouble(temperatureState.state);
        Long deltaTimeInMilliseconds = newDate.getTime() - temperatureState.last_changed.getTime();
        double deltaTimeInHours = ((double) deltaTimeInMilliseconds) / (1000.0 * 60.0 * 60.0);

        // Give the new state the old date, because it might be changed by another device as well

        // Adjust Temp
        if(heaterState.state.equals("on")) {
            double newTemp = currentTemp + deltaTimeInHours * onRate;
            result.add(new HassioState(this.tempSensorID, "" + newTemp, temperatureState.last_changed, new HassioSensorAttributes("temperature", "°C")));

            // Stop heating?
            if(currentTemp < targetTemp) {
                result.add(new HassioState(this.entityID, "eco", heaterState.last_changed, null));
            }
        } else if(heaterState.state.equals("eco")) {
            if(currentTemp > targetTemp) {
                // Do nothing
            } else {
                double newTemp = currentTemp + deltaTimeInHours * ecoRate;
                result.add(new HassioState(this.tempSensorID, "" + newTemp, temperatureState.last_changed, new HassioSensorAttributes("temperature", "°C")));

                // Start heating again?
                if(currentTemp > targetTemp + 1.0) {
                    result.add(new HassioState(this.entityID, "on", heaterState.last_changed, null));
                }
            }
        }

        return result;
    }
}
