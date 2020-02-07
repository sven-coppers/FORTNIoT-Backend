package sven.phd.iot.rules.triggers;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalendarAvailableTrigger extends Trigger {
    private String calendarIdentifier;

    public CalendarAvailableTrigger(String ruleIdentifier, String calendarIdentifier) {
        super(ruleIdentifier, "When " + calendarIdentifier + " becomes available");

        this.calendarIdentifier = calendarIdentifier;
    }

    @Override
    public boolean isTriggeredBy(HassioChange hassioChange) {
        if(hassioChange.entity_id.equals(this.calendarIdentifier)) {
            String oldState = hassioChange.hassioChangeData.oldState.state;
            String newState = hassioChange.hassioChangeData.newState.state;

            // If the state
            return !oldState.equals(newState);
        }

        return false;
    }

    @Override
    public List<HassioContext> verifyCondition(HashMap<String, HassioState> hassioStates) {
        HassioState hassioState = hassioStates.get(this.calendarIdentifier);

        if(hassioState.state.equals("off")) {
            List<HassioContext> triggerContexts = new ArrayList<>();
            triggerContexts.add(hassioState.context);
            return triggerContexts;
        }

        return null;
    }
}