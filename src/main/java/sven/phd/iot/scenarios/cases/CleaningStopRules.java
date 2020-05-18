package sven.phd.iot.scenarios.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.StateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.scenarios.RuleSet;
import sven.phd.iot.students.mathias.ActionsManager;

public class CleaningStopRules extends RuleSet {
    @Override
    public void createRules(RulesManager rulesManager, ActionsManager actionsManager) {
        Trigger stopCleaningTrigger = new PeopleHomeTrigger("rule.anyone_home_stop_cleaning", true);
        stopCleaningTrigger.addAction(new StateAction("return roomba downstairs to dock", CleaningDevices.ROOMBA_DOWNSTAIRS, "docked"));
        stopCleaningTrigger.addAction(new StateAction("return roomba upstairs to dock", CleaningDevices.ROOMBA_UPSTAIRS, "docked"));
        rulesManager.addRule(stopCleaningTrigger);
    }
}
