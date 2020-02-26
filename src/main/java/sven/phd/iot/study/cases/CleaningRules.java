package sven.phd.iot.study.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.StartCleaningAction;
import sven.phd.iot.rules.actions.StateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.study.StudyRuleSet;

public class CleaningRules extends StudyRuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
        Trigger nobodyHomeTrigger = new PeopleHomeTrigger("rule.nobody_home_cleaning", false);
        nobodyHomeTrigger.addAction(new StartCleaningAction("clean downstairs for two hours", CleaningDevices.ROOMBA_DOWNSTAIRS, "cleaning", 120.0));
        rulesManager.addRule(nobodyHomeTrigger);
    }
}
