package sven.phd.iot.rules.actions;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StateAction extends Action {
    protected final String deviceIdentifier;
    protected final String newState;

    public StateAction(String description, String deviceIdentifier, String newState) {
        super(description);
        this.deviceIdentifier = deviceIdentifier;
        this.newState = newState;
    }

    public List<HassioState> simulate(HassioRuleExecutionEvent hassioRuleExecutionEvent, HashMap<String, HassioState> hassioStates) {
        List<HassioState> newStates = new ArrayList<>();

        newStates.add(new HassioState(this.deviceIdentifier, newState, hassioRuleExecutionEvent.datetime, null));

        return newStates;
    }
}