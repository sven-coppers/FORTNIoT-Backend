package sven.phd.iot.rules.actions;

import sven.phd.iot.hassio.cleaning.HassioCleanerAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StartCleaningAction extends StateAction {
    private final double programLength;

    public StartCleaningAction(String description, String deviceIdentifier, String newState, double programLength) {
        super(description, deviceIdentifier, newState);
        this.programLength = programLength;
    }

    public List<HassioState> simulate(HassioRuleExecutionEvent hassioRuleExecutionEvent, HashMap<String, HassioState> hassioStates) {
        List<HassioState> newStates = new ArrayList<>();

        newStates.add(new HassioState(this.deviceIdentifier, newState, hassioRuleExecutionEvent.datetime, new HassioCleanerAttributes(programLength)));

        return newStates;
    }
}
