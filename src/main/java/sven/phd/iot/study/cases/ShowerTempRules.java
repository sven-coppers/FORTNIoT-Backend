package sven.phd.iot.study.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.ThermostatStateAction;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.study.StudyRuleSet;
import sven.phd.iot.study.StudyStateSet;

public class ShowerTempRules extends StudyRuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
        Trigger morningTrigger = new StateTrigger("rule.morning", WeekdayRoutineDevices.ROUTINE, "morning", "everyone is sleeping");
        morningTrigger.addAction(new ThermostatStateAction(ShowerTempDevices.SHOWER_THERMOSTAT, "shower thermostat", 23.0));
    }
}
