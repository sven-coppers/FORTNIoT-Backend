package sven.phd.iot.study.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.ThermostatStateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.students.bram.rules.triggers.ANDTrigger;
import sven.phd.iot.study.StudyRuleSet;

public class LivingTempRules extends StudyRuleSet {
    public static final String LIVING_TARGET_HIGHER = "system_rule.living_target_temp_higher";
    public static final String LIVING_TARGET_REACHED = "system_rule.living_target_temp_reached";

    @Override
    public void createRules(RulesManager rulesManager) {
        Trigger nobodyHomeTrigger = new PeopleHomeTrigger("rule.nobody_home_living", false);
        nobodyHomeTrigger.addAction(new ThermostatStateAction(LivingTempDevices.LIVING_THERMOSTAT, "living thermostat", 15.0));
        rulesManager.addRule(nobodyHomeTrigger);

        Trigger comesHomeTrigger = new PeopleHomeTrigger("rule.comes_home",true);
        comesHomeTrigger.addAction(new ThermostatStateAction(LivingTempDevices.LIVING_THERMOSTAT, "living thermostat", 21.0));
        rulesManager.addRule(comesHomeTrigger);

        ANDTrigger eveningTrigger = new ANDTrigger("rule.evening_living");
        eveningTrigger.addTrigger(new PeopleHomeTrigger("", true));
        eveningTrigger.addTrigger(new StateTrigger("", RoutineDevices.ROUTINE, "evening", "evening"));
        eveningTrigger.addAction(new ThermostatStateAction(LivingTempDevices.LIVING_THERMOSTAT, "living thermostat", 21.0));
        rulesManager.addRule(eveningTrigger);

        ANDTrigger morningTrigger = new ANDTrigger("rule.morning_living");
        morningTrigger.addTrigger(new PeopleHomeTrigger("", true));
        morningTrigger.addTrigger(new StateTrigger("rule.morning_living", RoutineDevices.ROUTINE, "morning", "morning"));
        morningTrigger.addAction(new ThermostatStateAction(LivingTempDevices.LIVING_THERMOSTAT, "living thermostat", 21.0));
        rulesManager.addRule(morningTrigger);

        Trigger sleepingTrigger = new StateTrigger("rule.sleeping_living", RoutineDevices.ROUTINE, "sleeping", "everyone is sleeping");
        sleepingTrigger.addAction(new ThermostatStateAction(LivingTempDevices.LIVING_THERMOSTAT, "living thermostat", 15.0));
        rulesManager.addRule(sleepingTrigger);

        Trigger targetTempReached = new ANDTrigger(LIVING_TARGET_REACHED);
        rulesManager.addRule(targetTempReached);
        targetTempReached.setTitle("IF target temperature reached THEN set floor heating to ECO");

        Trigger targetTempHigher = new ANDTrigger(LIVING_TARGET_HIGHER);
        rulesManager.addRule(targetTempHigher);
        targetTempHigher.setTitle("IF target temperature higher THEN set floor heating to HEATING");
    }
}