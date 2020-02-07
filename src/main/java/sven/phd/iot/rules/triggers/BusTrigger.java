package sven.phd.iot.rules.triggers;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BusTrigger extends Trigger {
    public BusTrigger(String ruleIdentifier) {
        super(ruleIdentifier, "When the bus arrives");
    }

    @Override
    public boolean isTriggeredBy(HassioChange hassioChange) {
        return hassioChange.entity_id.equals("sensor.agoralaan_diepenbeek");
    }

    public List<HassioContext> verifyCondition(HashMap<String, HassioState> hassioStateHashMap) {
        HassioState busState = hassioStateHashMap.get("sensor.agoralaan_diepenbeek");

        if(busState == null) return null;

        //HassioWeatherState oldState = (HassioWeatherState) busChange.hassioChangeData.oldState;
        //HassioWeatherState newState = (HassioWeatherState) busChange.hassioChangeData.newState;

        List<HassioContext> result = new ArrayList<>();

        result.add(busState.context);

        return result;
    }
}
