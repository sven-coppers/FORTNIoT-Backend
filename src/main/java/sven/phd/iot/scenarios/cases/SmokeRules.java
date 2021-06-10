package sven.phd.iot.scenarios.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.scenarios.RuleSet;

import java.awt.*;

public class SmokeRules extends RuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
        Trigger smokeTrigger = new StateTrigger("system_rule.smoke_lights", SmokeDevices.SENSOR_LIVING_SMOKE, "smoke detected", "smoke detected");
        smokeTrigger.addAction(new LightOnAction("turn on living spots", LightSimpleDevices.LIVING_SPOTS, Color.YELLOW, false));
        rulesManager.addRule(smokeTrigger);
        smokeTrigger.setTitle("smoke detected THEN turn on the living spots");
    }
}
