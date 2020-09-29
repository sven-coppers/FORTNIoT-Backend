package sven.phd.iot.scenarios.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.LightOffAction;
import sven.phd.iot.rules.actions.StateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.scenarios.RuleSet;
import sven.phd.iot.students.bram.rules.triggers.ANDTrigger;

public class RedundancyRules1 extends RuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
        // Rule 1
        Trigger sunRiseTrigger = new StateTrigger("rule.sun_rise", "sun.sun", "above_horizon", "sun rises");
        sunRiseTrigger.addAction(new LightOffAction("turn off living spots", RedundancyDevices.LIVING_SPOTS));
        rulesManager.addRule(sunRiseTrigger);

        // Rule 2
        ANDTrigger sunRisesAndSomebodyHomeTrigger = new ANDTrigger("rule.sun_rises_and_somebody_home");
        sunRisesAndSomebodyHomeTrigger.addTrigger(new StateTrigger("", "sun.sun", "above_horizon", "sun rises"));
        sunRisesAndSomebodyHomeTrigger.addTrigger(new PeopleHomeTrigger("", true));
        sunRisesAndSomebodyHomeTrigger.addAction(new StateAction("raise the rolling shutter", RedundancyDevices.LIVING_BLINDS, "raised"));
        rulesManager.addRule(sunRisesAndSomebodyHomeTrigger);

        // Rule 3
        Trigger somebodyHome = new PeopleHomeTrigger("rule.somebody_home", true);
        somebodyHome.addAction(new StateAction("turn on TV", RedundancyDevices.LIVING_TV, "on"));
        rulesManager.addRule(somebodyHome);

        // Rule 4
        Trigger tvOnTrigger = new StateTrigger("rule.tv_on", RedundancyDevices.LIVING_TV, "on", "TV on");
        tvOnTrigger.addAction(new LightOffAction("turn off living spots", RedundancyDevices.LIVING_SPOTS));
        rulesManager.addRule(tvOnTrigger);
    }
}
