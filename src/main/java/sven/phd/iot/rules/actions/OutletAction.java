package sven.phd.iot.rules.actions;

import sven.phd.iot.hassio.outlet.HassioOutletAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.List;

public class OutletAction extends Action {
    private final String deviceIdentifier;
    private final String newState;

    public OutletAction(String description, String deviceIdentifier, String newState) {
        super(description);
        this.deviceIdentifier = deviceIdentifier;
        this.newState = newState;
    }

    public List<HassioState> simulate(HassioRuleExecutionEvent hassioRuleExecutionEvent) {
        List<HassioState> newStates = new ArrayList<>();

        newStates.add(new HassioState(this.deviceIdentifier, newState, hassioRuleExecutionEvent.datetime, new HassioOutletAttributes()));

        return newStates;
    }
}