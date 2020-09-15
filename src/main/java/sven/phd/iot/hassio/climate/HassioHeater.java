package sven.phd.iot.hassio.climate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.Future;
import sven.phd.iot.rules.ActionExecution;
import sven.phd.iot.rules.RuleExecution;
import sven.phd.iot.rules.RulesManager;

import java.io.IOException;
import java.util.*;

public class HassioHeater extends HassioTemperatureModifier {
    private final double onRate;
    private final double ecoRate;
    private final boolean autoOn;
    private final boolean autoOff;


    public HassioHeater(String entityID, String friendlyName, double onRate, double ecoRate, String thermostatID, String tempSensorID, boolean autoOn, boolean autoOff) {
        super(entityID, friendlyName, thermostatID, tempSensorID);
        this.onRate = onRate;
        this.ecoRate = ecoRate;
        this.autoOn = autoOn;
        this.autoOff = autoOff;
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioHeaterAttributes.class);
    }

    @Override
    protected List<HassioState> predictTickFutureStates(Date newDate, Future future) {
        List<HassioState> resultingStates = new ArrayList<>();
        HashMap<String, HassioState> lastStates = future.getLastStates();

        HassioState thermostatState = lastStates.get(this.thermostatID);
        HassioState temperatureState = lastStates.get(this.tempSensorID);
        HassioState heaterState = lastStates.get(this.entityID);

        if(thermostatState == null || temperatureState == null || heaterState == null) return resultingStates;

        double targetTemp = Double.parseDouble(thermostatState.state);
        double currentTemp = Double.parseDouble(temperatureState.state);

        // Adjust heater state if needed
        if(autoOff && heaterState.state.equals("heating") && currentTemp > targetTemp) {
            HassioState newHeaterState = new HassioState(this.entityID, "eco", heaterState.getLastChanged(), new HassioHeaterAttributes());
            resultingStates.add(newHeaterState);

            RuleExecution newBehavior = new RuleExecution(newDate, this.entityID + "_stop_heating", temperatureState.context);
            newBehavior.addActionExecution(new ActionExecution("stop_heating", newHeaterState.context));
            newBehavior.addConditionContext(thermostatState.context);
            newBehavior.addConditionContext(temperatureState.context);
            future.addExecutionEvent(newBehavior);
        } else if(autoOn && heaterState.state.equals("eco") && currentTemp < targetTemp) {
            HassioState newHeaterState = new HassioState(this.entityID, "heating", heaterState.getLastChanged(), new HassioHeaterAttributes());
            resultingStates.add(newHeaterState);

            RuleExecution newBehavior = new RuleExecution(newDate, this.entityID + "_start_heating", temperatureState.context);
            newBehavior.addActionExecution(new ActionExecution("start_heating", newHeaterState.context));
            newBehavior.addConditionContext(thermostatState.context);
            newBehavior.addConditionContext(temperatureState.context);
            future.addExecutionEvent(newBehavior);
        }

        return resultingStates;
    }

    public double getOnRate() {
        return onRate;
    }

    public double getEcoRate() {
        return ecoRate;
    }
}
