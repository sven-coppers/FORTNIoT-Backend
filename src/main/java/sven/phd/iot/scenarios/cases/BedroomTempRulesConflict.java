package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.climate.HassioCoolerAttributes;
import sven.phd.iot.hassio.climate.HassioHeaterAttributes;
import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.StateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.rules.triggers.TemperatureReachesTrigger;
import sven.phd.iot.scenarios.RuleSet;
import sven.phd.iot.students.bram.rules.triggers.ANDTrigger;

public class BedroomTempRulesConflict extends RuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
        Trigger aircoTrigger = new TemperatureReachesTrigger("rule.too_warm", BedroomTempDevices.BEDROOM_TEMPERATURE, 25, false);
        aircoTrigger.addAction(new StateAction("turn on air conditioning", BedroomTempDevices.BEDROOM_AIRCO, "cooling", new HassioCoolerAttributes()));
        rulesManager.addRule(aircoTrigger);

        ANDTrigger morningTrigger = new ANDTrigger("rule.morning_bedroom");
        morningTrigger.addTrigger(new StateTrigger("", RoutineDevices.ROUTINE, "morning", "morning"));
        morningTrigger.addTrigger(new PeopleHomeTrigger("", true));
        morningTrigger.addAction(new StateAction("turn on heating", BedroomTempDevices.BEDROOM_HEATING, "heating", new HassioHeaterAttributes()));
        rulesManager.addRule(morningTrigger);
    }
}