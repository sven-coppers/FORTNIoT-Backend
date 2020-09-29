package sven.phd.iot.scenarios.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.LightOffAction;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.scenarios.RuleSet;

import java.awt.*;

public class RaceConditionRules extends RuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
        Trigger sunSetTrigger = new StateTrigger("rule.sun_set_lights", "sun.sun", "below_horizon", "sun sets");
        LightOnAction livingSpots = new LightOnAction("turn on the living spots", RaceConditionDevices.LIVING_SPOTS, Color.YELLOW, false);
        LightOnAction gardenLights = new LightOnAction("turn on the garden lights", RaceConditionDevices.GARDEN_LIGHTS, Color.YELLOW, false);
        sunSetTrigger.addAction(livingSpots);
        sunSetTrigger.addAction(gardenLights);
        rulesManager.addRule(sunSetTrigger);

        Trigger nobodyHomeTrigger = new PeopleHomeTrigger("rule.nobody_home_lights", false);
        LightOffAction kitchenSpotsOffAction = new LightOffAction("turn off kitchen spots", RaceConditionDevices.KITCHEN_SPOTS);
        LightOffAction livingSpotsOffAction = new LightOffAction("turn off living spots", RaceConditionDevices.LIVING_SPOTS);
        nobodyHomeTrigger.addAction(kitchenSpotsOffAction);
        nobodyHomeTrigger.addAction(livingSpotsOffAction);
        rulesManager.addRule(nobodyHomeTrigger);
    }
}