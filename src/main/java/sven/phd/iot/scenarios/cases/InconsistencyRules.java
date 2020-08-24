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

public class InconsistencyRules extends RuleSet {
    @Override
    public void createRules(RulesManager rulesManager, ActionExecutions actionsManager) {
        /*
        Trigger sunSetTrigger = new StateTrigger("rule.sun_set", "sun.sun", "below_horizon", "sun sets");
        //sunSetTrigger.addAction(new StateAction("raise the rolling shutter", InconsistencyDevices.LIVING_BLINDS, "raised"));
        sunSetTrigger.addAction(new LightOnAction("turn on living spots", InconsistencyDevices.LIVING_SPOTS, Color.PINK, false));
        rulesManager.addRule(sunSetTrigger);

        ANDTrigger sunSetAndNobodyHomeTrigger = new ANDTrigger("rule.sun_set_and_nobody_home");
        sunSetAndNobodyHomeTrigger.addTrigger(new StateTrigger("", "sun.sun", "below_horizon", "sun sets"));
        sunSetAndNobodyHomeTrigger.addTrigger(new PeopleHomeTrigger("", false));
        sunSetAndNobodyHomeTrigger.addAction(new StateAction("clean downstairs", InconsistencyDevices.ROOMBA_DOWNSTAIRS, "cleaning"));
        rulesManager.addRule(sunSetAndNobodyHomeTrigger);

        Trigger nobodyHomeTrigger = new PeopleHomeTrigger("rule.nobody_home_lights", false);
        nobodyHomeTrigger.addAction(new LightOffAction("turn off living spots", InconsistencyDevices.LIVING_SPOTS));
        nobodyHomeTrigger.addAction(new LightOffAction("turn off garden lights", InconsistencyDevices.GARDEN_LIGHTS));
        rulesManager.addRule(nobodyHomeTrigger);

        Trigger lightsOnTrigger = new StateTrigger("rule.lights_on", InconsistencyDevices.LIVING_SPOTS, "on", "living spots on");
        lightsOnTrigger.addAction(new StateAction("unlock the front door", InconsistencyDevices.FRONT_DOOR, "unlocked"));
        rulesManager.addRule(lightsOnTrigger);

        Trigger lightsOffTrigger = new StateTrigger("rule.lights_off", InconsistencyDevices.LIVING_SPOTS, "off", "living spots off");
        lightsOffTrigger.addAction(new StateAction("turn on tv", TVDevices.LIVING_TV, "on"));
        rulesManager.addRule(lightsOffTrigger);

        // TEST for garden lights
        Trigger gardenLightsOffTrigger = new StateTrigger("rule.garden_off", InconsistencyDevices.GARDEN_LIGHTS, "off", "IF garden lights off");
        gardenLightsOffTrigger.addAction(new StateAction("raise the rolling shutter", InconsistencyDevices.LIVING_BLINDS, "raised"));
        rulesManager.addRule(gardenLightsOffTrigger);

        /*
        Trigger blindsUpTrigger = new StateTrigger("rule.blinds_up", InconsistencyDevices.LIVING_BLINDS, "raised", "IF blinds raised");
        blindsUpTrigger.addAction(new LightOnAction("turn on living spots", InconsistencyDevices.LIVING_SPOTS, Color.PINK, false));
        rulesManager.addRule(blindsUpTrigger);*//*


        ANDTrigger andLightsOnTrigger = new ANDTrigger("rule.and_lights_on");
        andLightsOnTrigger.addTrigger(new StateTrigger("", InconsistencyDevices.LIVING_SPOTS, "on", "living spots on"));
        andLightsOnTrigger.addTrigger(new StateTrigger("", InconsistencyDevices.ROOMBA_DOWNSTAIRS, "cleaning", "roomba is cleaning"));
        andLightsOnTrigger.addAction(new StateAction("unlock the front door", InconsistencyDevices.FRONT_DOOR, "unlocked"));
        rulesManager.addRule(andLightsOnTrigger);

        ANDTrigger andLightsOffTrigger = new ANDTrigger("rule.and_lights_off");
        andLightsOffTrigger.addTrigger(new StateTrigger("", InconsistencyDevices.LIVING_SPOTS, "off", "living spots off"));
        andLightsOffTrigger.addTrigger(new StateTrigger("", InconsistencyDevices.ROOMBA_DOWNSTAIRS, "cleaning", "roomba is cleaning"));
        andLightsOffTrigger.addAction(new StateAction("turn on tv", TVDevices.LIVING_TV, "on"));
        rulesManager.addRule(andLightsOffTrigger);

        // Test for light conflict
        Trigger testTrigger = new StateTrigger("rule.test_trigger_living_spots", InconsistencyDevices.LIVING_TV, "on", "If living TV on");
        testTrigger.addAction(new LightOnAction("turn on living spots", InconsistencyDevices.LIVING_SPOTS, Color.CYAN, false));
        rulesManager.addRule(testTrigger);*/

        // REDUNDANCY TEST
        /*
        Trigger sunSetTrigger = new StateTrigger("rule.sun_set", "sun.sun", "below_horizon", "sun sets");
        //sunSetTrigger.addAction(new StateAction("raise the rolling shutter", InconsistencyDevices.LIVING_BLINDS, "raised"));
        sunSetTrigger.addAction(new LightOnAction("turn on living spots", InconsistencyDevices.LIVING_SPOTS, Color.PINK, false));
        rulesManager.addRule(sunSetTrigger);

        ANDTrigger sunSetAndNobodyHomeTrigger = new ANDTrigger("rule.sun_set_and_nobody_home");
        sunSetAndNobodyHomeTrigger.addTrigger(new StateTrigger("", "sun.sun", "below_horizon", "sun sets"));
        sunSetAndNobodyHomeTrigger.addTrigger(new PeopleHomeTrigger("", false));
        sunSetAndNobodyHomeTrigger.addAction(new StateAction("clean downstairs", InconsistencyDevices.ROOMBA_DOWNSTAIRS, "cleaning"));
        rulesManager.addRule(sunSetAndNobodyHomeTrigger);

        Trigger nobodyHomeTrigger = new PeopleHomeTrigger("rule.nobody_home_lights", false);
        nobodyHomeTrigger.addAction(new LightOffAction("turn off living spots", InconsistencyDevices.LIVING_SPOTS));
        nobodyHomeTrigger.addAction(new LightOffAction("turn off garden lights", InconsistencyDevices.GARDEN_LIGHTS));
        rulesManager.addRule(nobodyHomeTrigger);

        Trigger roombaCleaningTrigger = new StateTrigger("rule.roomba_cleaning", InconsistencyDevices.ROOMBA_DOWNSTAIRS, "cleaning", "IF roomba cleaning");
        roombaCleaningTrigger.addAction(new StateAction("raise the rolling shutter", InconsistencyDevices.LIVING_BLINDS, "raised"));
        rulesManager.addRule(roombaCleaningTrigger);

        Trigger gardenLightsOffTrigger = new StateTrigger("rule.garden_off", InconsistencyDevices.GARDEN_LIGHTS, "off", "IF garden lights off");
        gardenLightsOffTrigger.addAction(new LightOffAction("turn off living spots", InconsistencyDevices.LIVING_SPOTS));
        rulesManager.addRule(gardenLightsOffTrigger);

        Trigger blindsRaisedTrigger = new StateTrigger("rule.blinds_raised", InconsistencyDevices.LIVING_BLINDS, "raised", "IF blinds raised");
        blindsRaisedTrigger.addAction(new LightOnAction("turn on living spots", InconsistencyDevices.LIVING_SPOTS, Color.PINK, false));
        rulesManager.addRule(blindsRaisedTrigger);

         */

        // For the thesis screenshots INCONSISTENCY!!!
        ANDTrigger sunSetAndSomebodyHomeTrigger = new ANDTrigger("rule.sun_set_and_somebody_home");
        sunSetAndSomebodyHomeTrigger.addTrigger(new StateTrigger("", "sun.sun", "above_horizon", "sun rises"));
        sunSetAndSomebodyHomeTrigger.addTrigger(new PeopleHomeTrigger("", true));
        sunSetAndSomebodyHomeTrigger.addAction(new StateAction("turn on TV", InconsistencyDevices.LIVING_TV, "on"));
        rulesManager.addRule(sunSetAndSomebodyHomeTrigger);

        Trigger tvOnTrigger = new StateTrigger("rule.tv_on", InconsistencyDevices.LIVING_TV, "on", "IF TV on");
        tvOnTrigger.addAction(new StateAction("lower the rolling shutter", InconsistencyDevices.LIVING_BLINDS, "lowered"));
        tvOnTrigger.addAction(new LightOffAction("turn off living spots", InconsistencyDevices.LIVING_SPOTS));
        rulesManager.addRule(tvOnTrigger);

        ANDTrigger somebodyHomeAndBlindsLoweredTrigger = new ANDTrigger("rule.somebody_home_and_blinds_lowered");
        somebodyHomeAndBlindsLoweredTrigger.addTrigger(new StateTrigger("", InconsistencyDevices.LIVING_BLINDS, "lowered", "blinds lowered"));
        somebodyHomeAndBlindsLoweredTrigger.addTrigger(new PeopleHomeTrigger("", true));
        somebodyHomeAndBlindsLoweredTrigger.addAction(new LightOnAction("turn on living lights", InconsistencyDevices.LIVING_SPOTS, Color.BLUE, false));
        rulesManager.addRule(somebodyHomeAndBlindsLoweredTrigger);
    }
}




