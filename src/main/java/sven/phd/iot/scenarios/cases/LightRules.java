package sven.phd.iot.scenarios.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.LightOffAction;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.scenarios.RuleSet;
import sven.phd.iot.students.bram.rules.triggers.ANDTrigger;
import sven.phd.iot.students.mathias.ActionExecutions;

import java.awt.*;

public class LightRules extends RuleSet {
    @Override
    public void createRules(RulesManager rulesManager, ActionExecutions actionsManager) {
     /*   Trigger nobodyHomeTrigger = new PeopleHomeTrigger("rule.nobody_home_lights", false);
      //  nobodyHomeTrigger.addAction(new LightOffAction("turn off kitchen spots", LightDevices.KITCHEN_SPOTS));
        nobodyHomeTrigger.addAction(new LightOffAction("turn off living chandelier", LightDevices.LIVING_CHANDELIER));
        nobodyHomeTrigger.addAction(new LightOffAction("turn off living led strip", LightDevices.LIVING_LED_STRIPS));
        nobodyHomeTrigger.addAction(new LightOffAction("turn off standing lamp", LightDevices.LIVING_STANDING_LAMP));
        rulesManager.addRule(nobodyHomeTrigger); */

        Trigger sunRiseTrigger = new StateTrigger("rule.sun_rise_lights", "sun.sun", "above_horizon", "sun rises");
      //  sunRiseTrigger.addAction(new LightOffAction("turn off kitchen spots", LightDevices.KITCHEN_SPOTS));
        sunRiseTrigger.addAction(new LightOffAction("turn off living spots", LightSimpleDevices.LIVING_SPOTS));
        sunRiseTrigger.addAction(new LightOffAction("turn off standing lamp", LightDevices.LIVING_STANDING_LAMP));
        rulesManager.addRule(sunRiseTrigger);

        ANDTrigger sunSetTrigger = new ANDTrigger("rule.sun_set_lights");
        sunSetTrigger.addTrigger(new StateTrigger("", "sun.sun", "below_horizon", "sun sets"));
        sunSetTrigger.addTrigger(new PeopleHomeTrigger("", true));
      //  sunSetTrigger.addAction(new LightOnAction("turn on kitchen spots", LightDevices.KITCHEN_SPOTS, Color.YELLOW, false));
        sunSetTrigger.addAction(new LightOnAction("turn on living spots", LightSimpleDevices.LIVING_SPOTS, Color.YELLOW, false));
        sunSetTrigger.addAction(new LightOnAction("turn on standing lamp", LightDevices.LIVING_STANDING_LAMP, Color.YELLOW, false));
        rulesManager.addRule(sunSetTrigger);

        Trigger rainTrigger = new StateTrigger("rule.weather_rain_lamps", "weather.dark_sky", "light_rain", "going to rain");
        rainTrigger.addAction(new LightOnAction("flash the living led strip purple", LightDevices.LIVING_LED_STRIPS, Color.PINK, true));
        rulesManager.addRule(rainTrigger);

        // Mathias test trigger
        Trigger testTrigger = new StateTrigger("rule.test_lights", "sun.sun", "above_horizon", "TEST sun rises");
        testTrigger.addAction(new LightOnAction("turn on living spots", LightSimpleDevices.LIVING_SPOTS, Color.RED, true));
        rulesManager.addRule(testTrigger);

        Trigger testTrigger2 = new StateTrigger("rule.test_other_lights", "sun.sun", "above_horizon", "TEST 2 sun rises");
        testTrigger2.addAction(new LightOnAction("turn on living spots", LightSimpleDevices.LIVING_SPOTS, Color.BLUE, true));
        rulesManager.addRule(testTrigger2);

        Trigger nextLight = new StateTrigger("rule.responsive_living_room_light", LightSimpleDevices.LIVING_SPOTS, "on", "Living spots are on");
        nextLight.addAction(new LightOffAction("turn off living chandelier", LightDevices.LIVING_CHANDELIER));
        rulesManager.addRule(nextLight);

        Trigger chandelierRule = new StateTrigger("rule.chandelier_light", LightDevices.LIVING_STANDING_LAMP, "off", "Living standing lamp is off");
        chandelierRule.addAction(new LightOnAction("turn on living chandelier", LightDevices.LIVING_CHANDELIER, Color.CYAN, true));
        rulesManager.addRule(chandelierRule);
    }
}
