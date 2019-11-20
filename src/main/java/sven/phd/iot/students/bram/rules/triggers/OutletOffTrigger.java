package sven.phd.iot.students.bram.rules.triggers;


import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.weather.HassioWeatherState;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OutletOffTrigger extends Trigger {
    private String outletId;

    public OutletOffTrigger(String ruleIdentifier, String outletId) {
        super(ruleIdentifier, "When " + outletId + " toggles off");

        this.outletId = outletId;
    }

    @Override
    public boolean isInterested(HassioChange hassioChange) {
        if (hassioChange.entity_id.equals(this.outletId)) {
            String oldState = hassioChange.hassioChangeData.oldState.state;
            String newState = hassioChange.hassioChangeData.newState.state;

            // If the state
            return !oldState.equals(newState);
        }

        return false;
    }

    @Override
    public List<HassioContext> verify(HashMap<String, HassioState> hassioStates) {
        HassioState hassioState = hassioStates.get(this.outletId);

        if (hassioState.state.equals("off")) {
            List<HassioContext> triggerContexts = new ArrayList<>();
            triggerContexts.add(hassioState.context);
            return triggerContexts;
        }

        return null;
    }
}
