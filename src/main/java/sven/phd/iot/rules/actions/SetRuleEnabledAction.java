package sven.phd.iot.rules.actions;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.Action;

import java.util.HashMap;
import java.util.List;

public class SetRuleEnabledAction extends Action {
    private String ruleID;
    private boolean enabled;

    public SetRuleEnabledAction(String ruleID, boolean enabled) {
        super((enabled? "Enable " : "disable ") + "rule " + ruleID);

        this.ruleID = ruleID;
        this.enabled = enabled;
    }

    @Override
    public List<HassioState> simulate(HassioRuleExecutionEvent hassioRuleExecutionEvent, HashMap<String, HassioState> hassioStates) {
        return null;
    }
}
