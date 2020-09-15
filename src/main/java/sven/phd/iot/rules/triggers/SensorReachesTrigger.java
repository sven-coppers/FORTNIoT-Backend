package sven.phd.iot.rules.triggers;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SensorReachesTrigger extends Trigger {
    private int targetValue;
    private boolean decreasing;
    private String sensorIdentifier;

    public SensorReachesTrigger(String ruleIdentifier, String sensorIdentifier, int targetValue, boolean decreasing, String name, String unit) {
        super(ruleIdentifier, name + (decreasing? " drops below " : " reaches ")  + targetValue + unit);

        this.targetValue = targetValue;
        this.decreasing = decreasing;
        this.sensorIdentifier = sensorIdentifier;
    }

    @Override
    public boolean isTriggeredBy(HassioChange hassioChange) {
        if(hassioChange.entity_id.equals(this.sensorIdentifier)) {
            float oldValue = Float.parseFloat(hassioChange.hassioChangeData.oldState.state);
            float newValue = Float.parseFloat(hassioChange.hassioChangeData.newState.state);

            if(newValue == oldValue) return false;

            if(decreasing) {
                // Drops below
                if(newValue <= targetValue && oldValue >= targetValue) return true;
            } else {
                // Reaches
                if(newValue >= targetValue && oldValue <= targetValue) return true;
            }

            return false;
        }

        return false;
    }

    @Override
    public List<HassioContext> verifyCondition(HashMap<String, HassioState> hassioStates) {
        HassioState hassioState = hassioStates.get(this.sensorIdentifier);
        float value = Float.parseFloat(hassioState.state);

        if((decreasing && value <= targetValue) || (!decreasing && value >= targetValue)){
            List<HassioContext> triggerContexts = new ArrayList<>();
            triggerContexts.add(hassioState.context);
            return triggerContexts;
        }

        return null;
    }

}
