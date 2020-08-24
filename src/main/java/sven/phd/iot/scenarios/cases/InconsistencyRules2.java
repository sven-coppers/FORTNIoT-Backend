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

public class InconsistencyRules2 extends RuleSet {
    @Override
    public void createRules(RulesManager rulesManager, ActionExecutions actionsManager) {
        // Rule 1
        Trigger somebodyHomeTrigger = new PeopleHomeTrigger("rule.somebody_home_routine", true);
        somebodyHomeTrigger.addAction(new StateAction("unlock the front door", InconsistencyDevices.FRONT_DOOR, "unlocked"));
        somebodyHomeTrigger.addAction(new StateAction("turn on tv", TVDevices.LIVING_TV, "on"));
        somebodyHomeTrigger.addAction(new StateAction("docked downstairs", InconsistencyDevices.ROOMBA_DOWNSTAIRS, "docked"));
        rulesManager.addRule(somebodyHomeTrigger);

        // Rule 2
        ANDTrigger sunSetAndDoorUnlockedTrigger = new ANDTrigger("rule.sun_set_and_door_unlocked");
        sunSetAndDoorUnlockedTrigger.addTrigger(new StateTrigger("", "sun.sun", "below_horizon", "sun set"));
        sunSetAndDoorUnlockedTrigger.addTrigger(new StateTrigger("", InconsistencyDevices.FRONT_DOOR, "unlocked", "front door unlocked"));
        sunSetAndDoorUnlockedTrigger.addAction(new LightOnAction("turn on living spots", InconsistencyDevices.LIVING_SPOTS, Color.YELLOW, false));
        rulesManager.addRule(sunSetAndDoorUnlockedTrigger);

        // Rule 3
        Trigger tvOnTrigger = new StateTrigger("rule.tv_on", InconsistencyDevices.LIVING_TV, "on", "TV on");
        tvOnTrigger.addAction(new StateAction("lower the rolling shutter", InconsistencyDevices.LIVING_BLINDS, "lowered"));
        tvOnTrigger.addAction(new LightOffAction("turn off living spots", InconsistencyDevices.LIVING_SPOTS));
        rulesManager.addRule(tvOnTrigger);

        // Rule 4
        Trigger sunSetTrigger = new StateTrigger("rule.sun_set", "sun.sun", "below_horizon", "sun set");
        sunSetTrigger.addAction(new StateAction("lock the front door", InconsistencyDevices.FRONT_DOOR, "locked"));
        rulesManager.addRule(sunSetTrigger);
        /*
        ANDTrigger blindsLoweredAndLightsOff = new ANDTrigger("rule.blinds_lowered_and_lights_off");
        blindsLoweredAndLightsOff.addTrigger(new StateTrigger("rule.blinds_lowered", InconsistencyDevices.LIVING_BLINDS, "lowered", "blinds lowered"));
        blindsLoweredAndLightsOff.addTrigger(new StateTrigger("rule.living_spots_off", InconsistencyDevices.LIVING_SPOTS, "off", "living spots off"));
        blindsLoweredAndLightsOff.addAction(new StateAction("lock the front door", InconsistencyDevices.FRONT_DOOR, "locked"));
        rulesManager.addRule(blindsLoweredAndLightsOff);*/
    }
}




