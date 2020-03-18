package sven.phd.iot.study.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.StartCleaningAction;
import sven.phd.iot.rules.actions.StateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.SensorReachesTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.study.StudyRuleSet;

public class CleaningRules extends StudyRuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
        Trigger nobodyHomeTrigger = new PeopleHomeTrigger("rule.nobody_home_cleaning", false);
        nobodyHomeTrigger.addAction(new StateAction("clean upstairs", CleaningDevices.ROOMBA_UPSTAIRS, "cleaning"));
        rulesManager.addRule(nobodyHomeTrigger);

        Trigger batteryTrigger = new SensorReachesTrigger("system_rule.roomba_upstairs_empty", CleaningDevices.ROOMBA_UPSTAIRS_BATTERY, 15, true, "battery", "%");
        batteryTrigger.addAction(new StateAction("", CleaningDevices.ROOMBA_UPSTAIRS, "docked"));
        rulesManager.addRule(batteryTrigger);
        batteryTrigger.setTitle("IF Roomba upstairs battery < 15% THEN return to docking station");

        Trigger batteryDownstairsTrigger = new SensorReachesTrigger("system_rule.roomba_downstairs_empty", CleaningDevices.ROOMBA_DOWNSTAIRS_BATTERY, 15, true, "battery", "%");
        batteryDownstairsTrigger.addAction(new StateAction("", CleaningDevices.ROOMBA_DOWNSTAIRS, "docked"));
        rulesManager.addRule(batteryDownstairsTrigger);
        batteryDownstairsTrigger.setTitle("IF Roomba downstairs battery < 15% THEN return to docking station");

        Trigger sleepingTrigger = new StateTrigger("rule.sleeping_cleaning", RoutineDevices.ROUTINE, "sleeping", "everyone sleeping");
        sleepingTrigger.addAction(new StateAction("clean downstairs", CleaningDevices.ROOMBA_DOWNSTAIRS, "cleaning"));
        rulesManager.addRule(sleepingTrigger);
    }
}
