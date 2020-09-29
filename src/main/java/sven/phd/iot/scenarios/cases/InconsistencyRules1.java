package sven.phd.iot.scenarios.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.StateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.scenarios.RuleSet;
import sven.phd.iot.students.bram.rules.triggers.ANDTrigger;

public class InconsistencyRules1 extends RuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
        // Rule 1
        ANDTrigger sunRiseAndNobodyHomeTrigger = new ANDTrigger("rule.sun_rise_and_nobody_home");
        sunRiseAndNobodyHomeTrigger.addTrigger(new StateTrigger("", "sun.sun", "above_horizon", "sun rises"));
        sunRiseAndNobodyHomeTrigger.addTrigger(new PeopleHomeTrigger("", false));
        sunRiseAndNobodyHomeTrigger.addAction(new StateAction("clean downstairs", InconsistencyDevices.ROOMBA_DOWNSTAIRS, "cleaning"));
        rulesManager.addRule(sunRiseAndNobodyHomeTrigger);

        // Rule 2
        Trigger somebodyHomeTrigger = new PeopleHomeTrigger("rule.somebody_home_cleaning", true);
        somebodyHomeTrigger.addAction(new StateAction("docked downstairs", InconsistencyDevices.ROOMBA_DOWNSTAIRS, "docked"));
        rulesManager.addRule(somebodyHomeTrigger);

        // Rule 3 TODO
        Trigger beforeEventTrigger = new StateTrigger("rule.before_event", InconsistencyDevices.ROUTINE, "visitors", "visitors arrive soon");
        beforeEventTrigger.addAction(new StateAction("clean downstairs", InconsistencyDevices.ROOMBA_DOWNSTAIRS, "cleaning"));
        rulesManager.addRule(beforeEventTrigger);
    }
}




