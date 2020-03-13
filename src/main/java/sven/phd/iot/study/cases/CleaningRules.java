package sven.phd.iot.study.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.StartCleaningAction;
import sven.phd.iot.rules.actions.StateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.SensorReachesTrigger;
import sven.phd.iot.study.StudyRuleSet;

public class CleaningRules extends StudyRuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
        Trigger nobodyHomeTrigger = new PeopleHomeTrigger("rule.nobody_home_cleaning", false);
        nobodyHomeTrigger.addAction(new StateAction("clean downstairs", CleaningDevices.ROOMBA_DOWNSTAIRS, "cleaning"));
        rulesManager.addRule(nobodyHomeTrigger);

        Trigger batteryTrigger = new SensorReachesTrigger("system_rule.windy_blinds", CleaningDevices.ROOMBA_DOWNSTAIRS_BATTERY, 15, true, "battery", "%");
        batteryTrigger.addAction(new StateAction("return to docking station", CleaningDevices.ROOMBA_DOWNSTAIRS, "docked"));
        rulesManager.addRule(batteryTrigger);
        batteryTrigger.setTitle("Roomba built-in behavior");
    }
}
