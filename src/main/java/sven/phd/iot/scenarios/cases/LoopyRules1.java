package sven.phd.iot.scenarios.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.LightOffAction;
import sven.phd.iot.rules.actions.StateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.scenarios.RuleSet;
import sven.phd.iot.students.bram.rules.triggers.ANDTrigger;
import sven.phd.iot.students.mathias.ActionExecutions;

public class LoopyRules1 extends RuleSet {
    @Override
    public void createRules(RulesManager rulesManager, ActionExecutions actionsManager) {
        // Rule 1
        Trigger sunRiseTrigger = new StateTrigger("rule.sun_rise", "sun.sun", "above_horizon", "sun rises");
        sunRiseTrigger.addAction(new LightOffAction("turn off living spots", LoopyDevices.LIVING_SPOTS));
        rulesManager.addRule(sunRiseTrigger);

        // Rule 2
        ANDTrigger lightsOffAndSomebodyHomeTrigger = new ANDTrigger("rule.lights_off_and_somebody_home");
        lightsOffAndSomebodyHomeTrigger.addTrigger(new StateTrigger("", LoopyDevices.LIVING_SPOTS, "off", "living spots off"));
        lightsOffAndSomebodyHomeTrigger.addTrigger(new PeopleHomeTrigger("", true));
        lightsOffAndSomebodyHomeTrigger.addAction(new StateAction("raise the rolling shutter", LoopyDevices.LIVING_BLINDS, "raised"));
        rulesManager.addRule(lightsOffAndSomebodyHomeTrigger);

        // Rule 3
        Trigger blindsUpTrigger = new StateTrigger("rule.blinds_up", LoopyDevices.LIVING_BLINDS, "raised", "blinds raised");
        blindsUpTrigger.addAction(new LightOffAction("turn off living spots", LoopyDevices.LIVING_SPOTS));
        rulesManager.addRule(blindsUpTrigger);
    }
}
