package sven.phd.iot.rules.triggers;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.sun.HassioSunState;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Gets triggered as soon as the sun sets */
public class SunRiseTrigger extends Trigger {
    public SunRiseTrigger(String ruleIdentifier) {
        super(ruleIdentifier, "When the sun rises");
    }

    @Override
    public boolean isInterested(HassioChange hassioChange) {
        if(hassioChange.entity_id.equals("sun.sun")) {
            String oldState = hassioChange.hassioChangeData.oldState.state;
            String newState = hassioChange.hassioChangeData.newState.state;

            // If the state
            return !oldState.equals(newState);
        }

        return false;
    }

    public List<HassioContext> verify(HashMap<String, HassioState> hassioStateHashMap) {
        HassioSunState sunState = (HassioSunState) hassioStateHashMap.get("sun.sun");

        if(sunState == null) return null;

        if(sunState.state.equals("above_horizon")) {
            List<HassioContext> triggerContexts = new ArrayList<>();
            triggerContexts.add(sunState.context);
            return triggerContexts;
        }

        return null;
    }
}