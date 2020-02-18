package sven.phd.iot.rules.triggers;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioAbstractState;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.weather.HassioWeatherState;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateTempTrigger extends Trigger {
    private int intervalMinutes;
    private int rate;

    public UpdateTempTrigger(String ruleIdentifier, int intervalMinutes, int rate) {
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
    public List<HassioContext> verifyCondition(HashMap<String, HassioState> hassioStates) {
        HassioState hassioWeatherState = hassioStates.get("weather.dark_sky");
        List<HassioContext> triggerContexts = new ArrayList<>();
        triggerContexts.add(hassioWeatherState.context);
        return triggerContexts;
    }
}
