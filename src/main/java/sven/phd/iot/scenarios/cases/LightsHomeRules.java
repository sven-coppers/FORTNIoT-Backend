package sven.phd.iot.scenarios.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.LightOffAction;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.rules.actions.StateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.scenarios.RuleSet;
import sven.phd.iot.students.bram.rules.triggers.ANDTrigger;

import java.awt.*;


public class LightsHomeRules extends RuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
     //   ANDTrigger homeTrigger = new ANDTrigger("rule.lights_home");
     //   homeTrigger.addTrigger(new PeopleHomeTrigger("", true));
     //   homeTrigger.addTrigger(new StateTrigger("", LightSimpleDevices.LIVING_SPOTS, "off", "lights turned off"));
      //  homeTrigger.addAction(new LightOnAction("turn on the spots", LightSimpleDevices.LIVING_SPOTS, Color.YELLOW, false));
      //  rulesManager.addRule(homeTrigger);

        Trigger goneTrigger = new PeopleHomeTrigger("rule.lights_gone", false);
        goneTrigger.addAction(new LightOffAction("turn off the spots", LightSimpleDevices.LIVING_SPOTS));
        rulesManager.addRule(goneTrigger);
    }
}