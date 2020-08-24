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

public class LoopyRules extends RuleSet {
    @Override
    public void createRules(RulesManager rulesManager, ActionExecutions actionsManager) {
        /*
        Trigger sunSetTrigger = new StateTrigger("rule.sun_set", "sun.sun", "below_horizon", "sun sets");
        sunSetTrigger.addAction(new StateAction("raise the rolling shutter", LoopyDevices.LIVING_BLINDS, "raised"));
        rulesManager.addRule(sunSetTrigger);

        ANDTrigger sunSetAndNobodyHomeTrigger = new ANDTrigger("rule.sun_set_and_nobody_home");
        sunSetAndNobodyHomeTrigger.addTrigger(new StateTrigger("", "sun.sun", "below_horizon", "sun sets"));
        sunSetAndNobodyHomeTrigger.addTrigger(new PeopleHomeTrigger("", false));
        sunSetAndNobodyHomeTrigger.addAction(new StateAction("clean downstairs", LoopyDevices.ROOMBA_DOWNSTAIRS, "cleaning"));
        rulesManager.addRule(sunSetAndNobodyHomeTrigger);
        */
/*
        Trigger nobodyHomeTrigger = new PeopleHomeTrigger("rule.nobody_home_lights", false);
        nobodyHomeTrigger.addAction(new LightOffAction("turn off garden lights", LoopyDevices.GARDEN_LIGHTS));
        rulesManager.addRule(nobodyHomeTrigger);*/
/*
        Trigger blindsUpTrigger = new StateTrigger("rule.blinds_up", LoopyDevices.LIVING_BLINDS, "raised", "IF blinds raised");
        blindsUpTrigger.addAction(new LightOnAction("turn on living spots", LoopyDevices.LIVING_SPOTS, Color.PINK, false));
        blindsUpTrigger.addAction(new LightOnAction("turn on garden lights", LoopyDevices.GARDEN_LIGHTS, Color.BLUE, false));
        rulesManager.addRule(blindsUpTrigger);

        ANDTrigger lightsOnTrigger = new ANDTrigger("rule.lights_on");
        lightsOnTrigger.addTrigger(new StateTrigger("", LoopyDevices.LIVING_SPOTS, "on", "living spots on"));
        lightsOnTrigger.addTrigger(new StateTrigger("", LoopyDevices.ROOMBA_DOWNSTAIRS, "cleaning", "roomba is cleaning"));
        lightsOnTrigger.addAction(new StateAction("unlock the front door", LoopyDevices.FRONT_DOOR, "unlocked"));
        rulesManager.addRule(lightsOnTrigger);

        Trigger doorUnlockedTrigger = new StateTrigger("rule.door_unlocked", LoopyDevices.FRONT_DOOR, "unlocked", "IF front door unlocked");
        doorUnlockedTrigger.addAction(new StateAction("raise the rolling shutter", LoopyDevices.LIVING_BLINDS, "raised"));
        rulesManager.addRule(doorUnlockedTrigger);

        addAction(new LightOffAction("turn off garden lights", LoopyDevices.GARDEN_LIGHTS));*/
    }
}
