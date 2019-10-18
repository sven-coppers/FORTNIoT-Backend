package sven.phd.iot.rules.triggers;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.weather.HassioWeatherState;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeatherChangeTrigger extends Trigger {
    public WeatherChangeTrigger(String ruleIdentifier) {
        super(ruleIdentifier, "IF weather changes");
}

    @Override
    public boolean isInterested(HassioChange hassioChange) {
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
    protected List<HassioContext> verify(HashMap<String, HassioState> hassioStates) {
        HassioWeatherState hassioWeatherState = (HassioWeatherState) hassioStates.get("weather.dark_sky");
        List<HassioContext> triggerContexts = new ArrayList<>();
        triggerContexts.add(hassioWeatherState.context);
        return triggerContexts;
    }
}
