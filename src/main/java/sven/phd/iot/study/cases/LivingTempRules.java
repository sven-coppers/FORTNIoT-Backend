package sven.phd.iot.study.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.ThermostatStateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.study.StudyRuleSet;

public class LivingTempRules extends StudyRuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
        Trigger nobodyHomeTrigger = new PeopleHomeTrigger("rule.nobody_home_living", false);
        nobodyHomeTrigger.addAction(new ThermostatStateAction(LivingTempDevices.LIVING_THERMOSTAT, "living thermostat", 15.0));
        rulesManager.addRule(nobodyHomeTrigger);

        Trigger comesHomeTrigger = new PeopleHomeTrigger("rule.comes_home",true);
        comesHomeTrigger.addAction(new ThermostatStateAction(LivingTempDevices.LIVING_THERMOSTAT, "living thermostat", 21.0));
        rulesManager.addRule(comesHomeTrigger);

        Trigger eveningTrigger = new StateTrigger("rule.evening_living", WeekdayRoutineDevices.ROUTINE, "evening", "evening");
        eveningTrigger.addAction(new ThermostatStateAction(LivingTempDevices.LIVING_THERMOSTAT, "living thermostat", 21.0));
        rulesManager.addRule(eveningTrigger);

        Trigger sleepingTrigger = new StateTrigger("rule.sleeping_living", WeekdayRoutineDevices.ROUTINE, "sleeping", "everyone is sleeping");
        sleepingTrigger.addAction(new ThermostatStateAction(LivingTempDevices.LIVING_THERMOSTAT, "living thermostat", 15.0));
        rulesManager.addRule(sleepingTrigger);
    }
}