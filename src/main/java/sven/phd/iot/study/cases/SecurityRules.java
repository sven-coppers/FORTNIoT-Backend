package sven.phd.iot.study.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.StateAction;
import sven.phd.iot.rules.actions.ThermostatStateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.students.bram.rules.triggers.ANDTrigger;
import sven.phd.iot.study.StudyRuleSet;

public class SecurityRules extends StudyRuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
        Trigger nobodyHomeTrigger = new PeopleHomeTrigger("rule.nobody_home_security", false);
        nobodyHomeTrigger.addAction(new StateAction("lock the front door", SecurityDevices.FRONT_DOOR, "locked"));
        nobodyHomeTrigger.addAction(new StateAction("lock the back door", SecurityDevices.BACK_DOOR, "locked"));
        nobodyHomeTrigger.addAction(new StateAction("lock the garage door", SecurityDevices.GARAGE_DOOR, "locked"));
        rulesManager.addRule(nobodyHomeTrigger);

        Trigger comesHomeTrigger = new PeopleHomeTrigger("rule.comes_home_security",true);
        comesHomeTrigger.addAction(new StateAction("unlock the front door", SecurityDevices.FRONT_DOOR, "open"));
        rulesManager.addRule(comesHomeTrigger);

        ANDTrigger thiefTrigger = new ANDTrigger("rule.thief_detected");
        thiefTrigger.addTrigger(new PeopleHomeTrigger("", false));
        thiefTrigger.addTrigger(new StateTrigger("", SecurityDevices.LIVING_MOTION, "moving", "movement detected"));
        thiefTrigger.addAction(new StateAction("trigger the alarm siren", SecurityDevices.SIRENE, "sounding"));
        rulesManager.addRule(thiefTrigger);
    }
}