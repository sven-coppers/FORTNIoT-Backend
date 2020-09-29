package sven.phd.iot.scenarios.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.ThermostatStateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.scenarios.RuleSet;

public class ParentTempRules extends RuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
        Trigger nobodyHomeTrigger = new PeopleHomeTrigger("rule.nobody_home_parents", false);
        nobodyHomeTrigger.addAction(new ThermostatStateAction(ParentTempDevices.BEDROOM_MASTER_THERMOSTAT, "master bedroom thermostat", 15.0));
        rulesManager.addRule(nobodyHomeTrigger);

        Trigger eveningTrigger = new StateTrigger("rule.evening_parents", RoutineDevices.ROUTINE, "evening", "evening");
        eveningTrigger.addAction(new ThermostatStateAction(ParentTempDevices.BEDROOM_MASTER_THERMOSTAT, "master bedroom thermostat", 20.0));
        rulesManager.addRule(eveningTrigger);

        Trigger sleepingTrigger = new StateTrigger("rule.sleeping_parents", RoutineDevices.ROUTINE, "sleeping", "everyone is sleeping");
        sleepingTrigger.addAction(new ThermostatStateAction(ParentTempDevices.BEDROOM_MASTER_THERMOSTAT, "bedroom thermostat", 19.0));
        rulesManager.addRule(sleepingTrigger);
    }
}
