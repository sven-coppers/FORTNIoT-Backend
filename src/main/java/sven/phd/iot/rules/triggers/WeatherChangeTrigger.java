package sven.phd.iot.rules.triggers;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeatherChangeTrigger extends Trigger {
    public WeatherChangeTrigger(String ruleIdentifier) {
        super(ruleIdentifier, "weather changes");
}

    @Override
    public boolean isTriggeredBy(HassioChange hassioChange) {
        if(hassioChange.entity_id.equals("weather.dark_sky")) {
            String oldState = hassioChange.hassioChangeData.oldState.state;
            String newState = hassioChange.hassioChangeData.newState.state;

            if(!oldState.equals(newState)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<HassioState> verifyCondition(HashMap<String, HassioState> hassioStates) {
        HassioState hassioWeatherState = hassioStates.get("weather.dark_sky");
        List<HassioState> triggerContexts = new ArrayList<>();
        triggerContexts.add(hassioWeatherState);
        return triggerContexts;
    }
    @Override
    public List<String> getTriggeringEntities() {
        List<String> result = new ArrayList<>();
        result.add("weather.dark_sky");
        return result;
    }
}
