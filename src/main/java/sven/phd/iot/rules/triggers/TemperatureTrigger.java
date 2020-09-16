package sven.phd.iot.rules.triggers;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TemperatureTrigger extends Trigger {
    private int min;
    private int max;
    private String sensorIdentifier;

    public TemperatureTrigger(String ruleIdentifier, String sensorIdentifier, int min, int max) {
        super(ruleIdentifier, "temperature between [" + min + ", " + max + "]");

        this.min = min;
        this.max = max;
        this.sensorIdentifier = sensorIdentifier;
    }

    @Override
    public boolean isTriggeredBy(HashMap<String, HassioState> hassioStates, HassioChange hassioChange) {
        if(hassioChange.entity_id.equals(this.sensorIdentifier)) {
            float oldTemp = Float.parseFloat(hassioChange.hassioChangeData.oldState.state);
            float newTemp = Float.parseFloat(hassioChange.hassioChangeData.newState.state);

           // float newTemp = ((HassioWeatherAttributes) hassioChange.hassioChangeData.newState.attributes).temperature;

            if(newTemp <= min || newTemp >= max) {
                return true;
            }

            // If the state
            return oldTemp != newTemp;
        }

        return false;
    }

    @Override
    public List<HassioContext> verifyCondition(HashMap<String, HassioState> hassioStates) {
        HassioState hassioWeatherState = hassioStates.get(this.sensorIdentifier);
        float temperature = Float.parseFloat(hassioWeatherState.state);

        if(temperature >= min && temperature <= max) {
            List<HassioContext> triggerContexts = new ArrayList<>();
            triggerContexts.add(hassioWeatherState.context);
            return triggerContexts;
        }

        return null;
    }
}
