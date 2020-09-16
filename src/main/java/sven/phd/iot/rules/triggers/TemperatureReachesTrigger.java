package sven.phd.iot.rules.triggers;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TemperatureReachesTrigger extends Trigger {
    private float targetTemp;
    private boolean decreasing;
    private String sensorIdentifier;
    private String targetTempIdentifier;

    /**
     * Choose the target temp now
     * @param ruleIdentifier
     * @param sensorIdentifier
     * @param targetTemp
     * @param decreasing
     */
    public TemperatureReachesTrigger(String ruleIdentifier, String sensorIdentifier, float targetTemp, boolean decreasing) {
        super(ruleIdentifier, (decreasing? "temperature drops below " : "temperature reaches ")  + targetTemp + "°C");

        this.targetTemp = targetTemp;
        this.decreasing = decreasing;
        this.sensorIdentifier = sensorIdentifier;
        this.targetTempIdentifier = null;
    }

    /**
     * Use an external sensor to determine the target temp
     * @param ruleIdentifier
     * @param sensorIdentifier
     * @param targetTempIdentifier
     * @param decreasing
     */
    public TemperatureReachesTrigger(String ruleIdentifier, String sensorIdentifier, String targetTempIdentifier, boolean decreasing) {
        super(ruleIdentifier, (decreasing? "temperature too low " : "target temperature reached ") + "°C");

        this.decreasing = decreasing;
        this.sensorIdentifier = sensorIdentifier;
        this.targetTempIdentifier = targetTempIdentifier;
    }

    @Override
    public boolean isTriggeredBy(HashMap<String, HassioState> hassioStates, HassioChange hassioChange) {
        if(hassioChange.entity_id.equals(this.sensorIdentifier)) {
            float oldTemp = Float.parseFloat(hassioChange.hassioChangeData.oldState.state);
            float newTemp = Float.parseFloat(hassioChange.hassioChangeData.oldState.state);
            float targetTemp = this.targetTemp;

            if(targetTempIdentifier != null && hassioStates.get(targetTempIdentifier) != null) {
                targetTemp = Float.parseFloat(hassioStates.get(targetTempIdentifier).state);
            }

            if(decreasing && oldTemp > targetTemp && newTemp < targetTemp) return true;
            if(!decreasing && oldTemp < targetTemp && newTemp > targetTemp) return true;
        }

        if(targetTempIdentifier != null && hassioChange.entity_id.equals(targetTempIdentifier)) {
            float oldTarget = Float.parseFloat(hassioChange.hassioChangeData.oldState.state);
            float newTarget = Float.parseFloat(hassioChange.hassioChangeData.oldState.state);
            float temp = Float.parseFloat(hassioStates.get(targetTempIdentifier).state);

            if(decreasing && temp > oldTarget && temp < newTarget) return true;
            if(!decreasing && temp < oldTarget && temp > newTarget) return true;
        }

        return false;
    }

    @Override
    public List<HassioContext> verifyCondition(HashMap<String, HassioState> hassioStates) {
        List<HassioContext> conditionSatisfyingContexts = new ArrayList<>();

        if(this.targetTempIdentifier != null && hassioStates.get(targetTempIdentifier) != null) {
            this.targetTemp = Float.parseFloat(hassioStates.get(targetTempIdentifier).state);
            conditionSatisfyingContexts.add(hassioStates.get(targetTempIdentifier).context);
        }

        if(this.sensorIdentifier != null && hassioStates.get(sensorIdentifier) != null) {
            float currentTemp = Float.parseFloat(hassioStates.get(sensorIdentifier).state);
            conditionSatisfyingContexts.add(hassioStates.get(sensorIdentifier).context);

            if((decreasing && currentTemp <= targetTemp) || (!decreasing && currentTemp >= targetTemp)){
                return conditionSatisfyingContexts;
            }
        }

        return null;
    }
}

       /* if(hassioChange.entity_id.equals(this.sensorIdentifier)) {
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
        } */