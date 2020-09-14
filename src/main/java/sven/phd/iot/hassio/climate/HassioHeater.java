package sven.phd.iot.hassio.climate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.ActionExecution;
import sven.phd.iot.rules.RuleExecution;
import sven.phd.iot.rules.RulesManager;

import java.io.IOException;
import java.util.*;

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
    protected List<RuleExecution> predictImplicitRules(Date newDate, HashMap<String, HassioState> hassioStates) {
        List<RuleExecution> result = new ArrayList<>();
        HassioState thermostatState = hassioStates.get(this.thermostatID);
        HassioState temperatureState = hassioStates.get(this.tempSensorID);
        HassioState heaterState = hassioStates.get(this.entityID);

        if(thermostatState == null || temperatureState == null || heaterState == null) return result;

        double targetTemp = Double.parseDouble(thermostatState.state);
        double currentTemp = Double.parseDouble(temperatureState.state);

        // Adjust heater state if needed
        if(heaterState.state.equals("heating") && currentTemp > targetTemp) {
            hassioStates.put(this.entityID, new HassioState(this.entityID, "eco", heaterState.getLastChanged(), new HassioHeaterAttributes()));

            RuleExecution newBehavior = new RuleExecution(newDate);
            newBehavior.addActionExecution(new ActionExecution("stop_heating", hassioStates.get(this.entityID).context));
            newBehavior.addTriggerContext(thermostatState.context);
            newBehavior.addTriggerContext(temperatureState.context);
            result.add(newBehavior);
        } else if(heaterState.state.equals("eco") && currentTemp < targetTemp) {
            hassioStates.put(this.entityID, new HassioState(this.entityID, "heating", heaterState.getLastChanged(), new HassioHeaterAttributes()));

            RuleExecution newBehavior = new RuleExecution(newDate);
            newBehavior.addActionExecution(new ActionExecution("start_heating", hassioStates.get(this.entityID).context));
            newBehavior.addTriggerContext(thermostatState.context);
            newBehavior.addTriggerContext(temperatureState.context);
            result.add(newBehavior);
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
