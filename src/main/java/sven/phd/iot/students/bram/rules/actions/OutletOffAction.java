package sven.phd.iot.students.bram.rules.actions;

import sven.phd.iot.hassio.outlet.HassioOutletAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.List;

public class OutletOffAction extends Action {
    private final String deviceIdentifier;

    public OutletOffAction(String deviceIdentifier) {
        super("Turn off " + deviceIdentifier);
        this.deviceIdentifier = deviceIdentifier;
    }

    public List<HassioState> simulate(HassioRuleExecutionEvent hassioRuleExecutionEvent) {
        List<HassioState> newStates = new ArrayList<>();
        
        newStates.add(new HassioState(deviceIdentifier, "off", hassioRuleExecutionEvent.datetime, new HassioOutletAttributes()));

        return newStates;
    }
}
