package sven.phd.iot.study.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.StateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.study.StudyRuleSet;

public class CleaningStopRules extends StudyRuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
        Trigger stopCleaningTrigger = new PeopleHomeTrigger("rule.anyone_home_stop_cleaning", true);
        stopCleaningTrigger.addAction(new StateAction("return roomba downstairs to dock", CleaningDevices.ROOMBA_DOWNSTAIRS, "docked"));
        stopCleaningTrigger.addAction(new StateAction("return roomba upstairs to dock", CleaningDevices.ROOMBA_UPSTAIRS, "docked"));
        rulesManager.addRule(stopCleaningTrigger);
    }
}
