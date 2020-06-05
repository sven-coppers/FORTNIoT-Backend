package sven.phd.iot.rules.actions;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.Action;

import java.util.HashMap;
import java.util.List;

public class SetActionEnabledAction extends Action {
    private String ruleID;
    private String actionID;
    private boolean enabled;

    public SetActionEnabledAction(String ruleID, String actionID, boolean enabled) {
        super((enabled? "Enable " : "disbale ") + "action " + actionID + " of rule " + ruleID);

        this.ruleID = ruleID;
        this.actionID = actionID;
        this.enabled = enabled;
    }

    @Override
    public List<HassioState> simulate(HassioRuleExecutionEvent hassioRuleExecutionEvent, HashMap<String, HassioState> hassioStates) {
        return null;
    }
}
