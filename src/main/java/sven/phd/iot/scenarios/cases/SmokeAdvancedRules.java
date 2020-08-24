package sven.phd.iot.scenarios.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.rules.actions.StateAction;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.scenarios.RuleSet;
import sven.phd.iot.students.mathias.ActionExecutions;

import java.awt.*;

public class SmokeAdvancedRules extends RuleSet {
    @Override
    public void createRules(RulesManager rulesManager, ActionExecutions actionsManager) {
        Trigger smokeTrigger = new StateTrigger("system_rule.smoke_unlock", SmokeDevices.SENSOR_LIVING_SMOKE, "smoke detected", "smoke detected");
        smokeTrigger.addAction(new LightOnAction("turn on living spots", LightSimpleDevices.LIVING_SPOTS, Color.YELLOW, false));
        smokeTrigger.addAction(new StateAction("unlock the front door", SecurityDevices.FRONT_DOOR, "unlock"));
        smokeTrigger.addAction(new StateAction("unlock the back door", SecurityDevices.BACK_DOOR, "unlock"));
        smokeTrigger.addAction(new StateAction("unlock the garage door", SecurityDevices.GARAGE_DOOR, "unlock"));
        rulesManager.addRule(smokeTrigger);
        smokeTrigger.setTitle("IF smoke detected THEN unlock all doors");
    }
}
