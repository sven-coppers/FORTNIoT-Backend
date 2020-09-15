package sven.phd.iot.hassio.climate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import sven.phd.iot.hassio.sensor.HassioSensorAttributes;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.Future;
import sven.phd.iot.rules.ActionExecution;
import sven.phd.iot.rules.RuleExecution;
import sven.phd.iot.rules.RulesManager;

import java.io.IOException;
import java.util.*;

public class HassioCooler extends HassioTemperatureModifier {
    private final double onRate;
    private final double ecoRate;
    private final boolean autoOn;
    private final boolean autoOff;

    public HassioCooler(String entityID, String friendlyName, double onRate, double ecoRate, String thermostatID, String tempSensorID, boolean autoOn, boolean autoOff) {
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
        HassioState coolerState = lastStates.get(this.entityID);

        if(thermostatState == null || temperatureState == null || coolerState == null) return resultingStates;

        double targetTemp = Double.parseDouble(thermostatState.state);
        double currentTemp = Double.parseDouble(temperatureState.state);

        // Adjust heater state if needed
        if(autoOff && coolerState.state.equals("cooling") && currentTemp < targetTemp) {
            HassioState newCoolerState = new HassioState(this.entityID, "eco", coolerState.getLastChanged(), new HassioHeaterAttributes());
            resultingStates.add(newCoolerState);

            RuleExecution newBehavior = new RuleExecution(newDate,this.entityID + "_stop_cooling", temperatureState.context);
            newBehavior.addActionExecution(new ActionExecution("stop_cooling", newCoolerState.context));
            newBehavior.addConditionContext(thermostatState.context);
            newBehavior.addConditionContext(temperatureState.context);
            future.addExecutionEvent(newBehavior);
        } else if(autoOn && coolerState.state.equals("cooling") && currentTemp > targetTemp) {
            HassioState newCoolerState = new HassioState(this.entityID, "cooling", coolerState.getLastChanged(), new HassioHeaterAttributes());
            resultingStates.add(newCoolerState);

            RuleExecution newBehavior = new RuleExecution(newDate, this.entityID + "_start_cooling", temperatureState.context);
            newBehavior.addActionExecution(new ActionExecution("start_cooling", newCoolerState.context));
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
