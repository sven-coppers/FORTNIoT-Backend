package sven.phd.iot.rules.triggers;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TemperatureReachesTrigger extends Trigger {
    private int targetTemp;
    private boolean decreasing;
    private String sensorIdentifier;

    public TemperatureReachesTrigger(String ruleIdentifier, String sensorIdentifier, int targetTemp, boolean decreasing) {
        super(ruleIdentifier, (decreasing? "temperature drops below " : "temperature reaches ")  + targetTemp + "°C");

        this.targetTemp = targetTemp;
        this.decreasing = decreasing;
        this.sensorIdentifier = sensorIdentifier;
    }

    @Override
    public boolean isTriggeredBy(HassioChange hassioChange) {
        if(hassioChange.entity_id.equals(this.sensorIdentifier)) {
            float oldTemp = Float.parseFloat(hassioChange.hassioChangeData.oldState.state);
            float newTemp = Float.parseFloat(hassioChange.hassioChangeData.newState.state);

            if(newTemp == oldTemp) return false;

            if(decreasing) {
                // Drops below
                if(newTemp <= targetTemp && oldTemp >= targetTemp) return true;
            } else {
                // Reaches
                if(newTemp >= targetTemp && oldTemp <= targetTemp) return true;
            }

            return false;
        }

        return false;
    }

    @Override
    public List<HassioContext> verifyCondition(HashMap<String, HassioState> hassioStates) {
        HassioState hassioState = hassioStates.get(this.sensorIdentifier);
        float temperature = Float.parseFloat(hassioState.state);

        if((decreasing && temperature <= targetTemp) || (!decreasing && temperature >= targetTemp)){
            List<HassioContext> triggerContexts = new ArrayList<>();
            triggerContexts.add(hassioState.context);
            return triggerContexts;
        }

        return null;
    }
    @Override
    public List<String> getTriggeringEntities() {
        List<String> result = new ArrayList<>();
        result.add(sensorIdentifier);
        return result;
    }
}
