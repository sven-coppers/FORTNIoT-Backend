package sven.phd.iot.scenarios.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.ThermostatStateAction;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.scenarios.RuleSet;
import sven.phd.iot.students.mathias.ActionsManager;

public class ShowerTempRules extends RuleSet {
    @Override
    public void createRules(RulesManager rulesManager, ActionsManager actionsManager) {
        Trigger morningTrigger = new StateTrigger("rule.morning", RoutineDevices.ROUTINE, "morning", "everyone is sleeping");
        morningTrigger.addAction(new ThermostatStateAction(ShowerTempDevices.SHOWER_THERMOSTAT, "shower thermostat", 23.0));
    }
}
