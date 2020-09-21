package sven.phd.iot.scenarios.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.LightOffAction;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.rules.actions.StateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.scenarios.RuleSet;
import sven.phd.iot.students.bram.rules.triggers.ANDTrigger;
import sven.phd.iot.students.mathias.ActionExecutions;

import java.awt.*;


public class TVRulesInconsistency extends RuleSet {
    @Override
    public void createRules(RulesManager rulesManager, ActionExecutions actionsManager) {
        ANDTrigger leisure = new ANDTrigger("rule.leisure");
        leisure.addTrigger(new PeopleHomeTrigger("", true));
        leisure.addTrigger(new StateTrigger("", RoutineDevices.ROUTINE, "evening", "routine = evening"));
        leisure.addAction(new StateAction("turn on tv", TVDevices.LIVING_TV, "on"));
        leisure.addAction(new LightOnAction("turn on the spots", LightSimpleDevices.LIVING_SPOTS, Color.YELLOW, false));
        rulesManager.addRule(leisure);

        Trigger sleepingTrigger = new StateTrigger("", RoutineDevices.ROUTINE, "sleeping", "routine = sleeping");
        sleepingTrigger.addAction(new StateAction("turn off tv", TVDevices.LIVING_TV, "off"));
        sleepingTrigger.addAction(new LightOffAction("turn off the spots", LightSimpleDevices.LIVING_SPOTS));
        rulesManager.addRule(sleepingTrigger);
    }
}