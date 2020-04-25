package sven.phd.iot.hassio.climate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.sensor.HassioSensorAttributes;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.ImplicitBehaviorEvent;
import sven.phd.iot.rules.RulesManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HassioHeater extends HassioTemperatureModifier {
    private final double onRate;
    private final double ecoRate;

    private String startHeatingRule = RulesManager.RULE_IMPLICIT_BEHAVIOR;
    private String stopHeatingRule = RulesManager.RULE_IMPLICIT_BEHAVIOR;

    public HassioHeater(String entityID, String friendlyName, double onRate, double ecoRate, String thermostatID, String tempSensorID) {
        super(entityID, friendlyName, thermostatID, tempSensorID);
        this.onRate = onRate;
        this.ecoRate = ecoRate;
    }

    public HassioHeater(String entityID, String friendlyName, double onRate, double ecoRate, String thermostatID, String tempSensorID, String livingTargetHigher, String livingTargetReached) {
        super(entityID, friendlyName, thermostatID, tempSensorID);
        this.onRate = onRate;
        this.ecoRate = ecoRate;

        this.startHeatingRule = livingTargetHigher;
        this.stopHeatingRule = livingTargetReached;
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

            ImplicitBehaviorEvent newHeaterState = new ImplicitBehaviorEvent(this.stopHeatingRule, newDate);
            newHeaterState.addActionDeviceID(this.entityID);
            newHeaterState.addTriggerDeviceID(this.thermostatID);
            newHeaterState.addTriggerDeviceID(this.tempSensorID);
            result.add(newHeaterState);
        } else if(heaterState.state.equals("eco") && currentTemp < targetTemp) {
            hassioStates.put(this.entityID, new HassioState(this.entityID, "heating", heaterState.getLastChanged(), new HassioHeaterAttributes(this.onRate)));

            ImplicitBehaviorEvent newHeaterState = new ImplicitBehaviorEvent(this.startHeatingRule, newDate);
            newHeaterState.addActionDeviceID(this.entityID);
            newHeaterState.addTriggerDeviceID(this.thermostatID);
            newHeaterState.addTriggerDeviceID(this.tempSensorID);
            result.add(newHeaterState);
        }

        return result;
    }
}
