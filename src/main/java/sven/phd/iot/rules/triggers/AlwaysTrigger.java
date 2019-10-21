package sven.phd.iot.rules.triggers;

import sven.phd.iot.hassio.calendar.HassioCalendarState;
import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlwaysTrigger extends Trigger {
    public AlwaysTrigger(String ruleIdentifier) {
        super(ruleIdentifier, "When any state has changed");
    }

    @Override
    public boolean isInterested(HassioChange hassioChange) {
        return false;
    }

    @Override
    protected List<HassioContext> verify(HashMap<String, HassioState> hassioStates) {

        return new ArrayList<>();
    }
}