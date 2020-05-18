package sven.phd.iot.study.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.LightOffAction;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.students.bram.rules.triggers.ANDTrigger;
import sven.phd.iot.students.mathias.ActionsManager;
import sven.phd.iot.study.StudyRuleSet;

import java.awt.*;

public class LightSimpleRules extends StudyRuleSet {
    @Override
    public void createRules(RulesManager rulesManager, ActionsManager actionsManager) {
        Trigger sunRiseTrigger = new StateTrigger("rule.sun_rise_lights", "sun.sun", "above_horizon", "sun rises");
        LightOffAction lof_livingSpots = new LightOffAction("turn off the living spots", LightSimpleDevices.LIVING_SPOTS);
        sunRiseTrigger.addAction(lof_livingSpots);
        rulesManager.addRule(sunRiseTrigger);
        actionsManager.addAction(lof_livingSpots);

        Trigger sunSetTrigger = new StateTrigger("rule.sun_set_lights", "sun.sun", "below_horizon", "sun sets");
        LightOnAction lon_livingSpots = new LightOnAction("turn on the living spots", LightSimpleDevices.LIVING_SPOTS, Color.YELLOW, false);
        sunSetTrigger.addAction(lon_livingSpots);
        rulesManager.addRule(sunSetTrigger);
        actionsManager.addAction(lon_livingSpots);
    }
}
