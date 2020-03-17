package sven.phd.iot.study.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.LightOffAction;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.students.bram.rules.triggers.ANDTrigger;
import sven.phd.iot.study.StudyRuleSet;

import java.awt.*;

public class LightSimpleRules extends StudyRuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
        Trigger sunRiseTrigger = new StateTrigger("rule.sun_rise_lights", "sun.sun", "above_horizon", "sun rises");
        sunRiseTrigger.addAction(new LightOffAction("turn off the living spots", LightSimpleDevices.LIVING_SPOTS));
        rulesManager.addRule(sunRiseTrigger);

        Trigger sunSetTrigger = new StateTrigger("rule.sun_set_lights", "sun.sun", "below_horizon", "sun sets");
        sunSetTrigger.addAction(new LightOnAction("turn on the living spots", LightSimpleDevices.LIVING_SPOTS, Color.YELLOW, false));
        rulesManager.addRule(sunSetTrigger);
    }
}