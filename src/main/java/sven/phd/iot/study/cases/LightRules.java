package sven.phd.iot.study.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.LightOffAction;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.rules.actions.ThermostatStateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.students.bram.rules.triggers.ANDTrigger;
import sven.phd.iot.students.mathias.ActionsManager;
import sven.phd.iot.study.StudyRuleSet;

import java.awt.*;

public class LightRules extends StudyRuleSet {
    @Override
    public void createRules(RulesManager rulesManager, ActionsManager actionsManager) {
     /*   Trigger nobodyHomeTrigger = new PeopleHomeTrigger("rule.nobody_home_lights", false);
      //  nobodyHomeTrigger.addAction(new LightOffAction("turn off kitchen spots", LightDevices.KITCHEN_SPOTS));
        nobodyHomeTrigger.addAction(new LightOffAction("turn off living chandelier", LightDevices.LIVING_CHANDELIER));
        nobodyHomeTrigger.addAction(new LightOffAction("turn off living led strip", LightDevices.LIVING_LED_STRIPS));
        nobodyHomeTrigger.addAction(new LightOffAction("turn off standing lamp", LightDevices.LIVING_STANDING_LAMP));
        rulesManager.addRule(nobodyHomeTrigger); */

        Trigger sunRiseTrigger = new StateTrigger("rule.sun_rise_lights", "sun.sun", "above_horizon", "sun rises");
      //  sunRiseTrigger.addAction(new LightOffAction("turn off kitchen spots", LightDevices.KITCHEN_SPOTS));
        LightOffAction lof_livingSpots = new LightOffAction("turn off living spots", LightSimpleDevices.LIVING_SPOTS);
        LightOffAction lof_livingSlamp = new LightOffAction("turn off standing lamp", LightDevices.LIVING_STANDING_LAMP);
        sunRiseTrigger.addAction(lof_livingSpots);
        sunRiseTrigger.addAction(lof_livingSlamp);
        rulesManager.addRule(sunRiseTrigger);
        actionsManager.addAction(lof_livingSpots);
        actionsManager.addAction(lof_livingSlamp);

        ANDTrigger sunSetTrigger = new ANDTrigger("rule.sun_set_lights");
        sunSetTrigger.addTrigger(new StateTrigger("", "sun.sun", "below_horizon", "sun sets"));
        sunSetTrigger.addTrigger(new PeopleHomeTrigger("", true));
      //  sunSetTrigger.addAction(new LightOnAction("turn on kitchen spots", LightDevices.KITCHEN_SPOTS, Color.YELLOW, false));
        LightOnAction lon_livingSpots = new LightOnAction("turn on living spots", LightSimpleDevices.LIVING_SPOTS, Color.YELLOW, false);
        LightOnAction lon_standingLamp = new LightOnAction("turn on standing lamp", LightDevices.LIVING_STANDING_LAMP, Color.YELLOW, false);
        sunSetTrigger.addAction(lon_livingSpots);
        sunSetTrigger.addAction(lon_standingLamp);
        rulesManager.addRule(sunSetTrigger);
        actionsManager.addAction(lon_livingSpots);
        actionsManager.addAction(lon_standingLamp);

        Trigger rainTrigger = new StateTrigger("rule.weather_rain_lamps", "weather.dark_sky", "light_rain", "going to rain");
        LightOnAction lon_livingLed = new LightOnAction("flash the living led strip purple", LightDevices.LIVING_LED_STRIPS, Color.PINK, true);
        rainTrigger.addAction(lon_livingLed);
        rulesManager.addRule(rainTrigger);
        actionsManager.addAction(lon_livingLed);

        // Mathias test trigger
        Trigger testTrigger = new StateTrigger("rule.test_lights", "sun.sun", "above_horizon", "TEST sun rises");
        LightOnAction lon_testSpots = new LightOnAction("turn on living spots", LightSimpleDevices.LIVING_SPOTS, Color.RED, true);
        testTrigger.addAction(lon_testSpots);
        rulesManager.addRule(testTrigger);
        actionsManager.addAction(lon_testSpots);

        Trigger testTrigger2 = new StateTrigger("rule.test_other_lights", "sun.sun", "above_horizon", "TEST 2 sun rises");
        LightOnAction lon_test2Spots = new LightOnAction("turn on living spots", LightSimpleDevices.LIVING_SPOTS, Color.BLUE, true);
        testTrigger2.addAction(lon_test2Spots);
        rulesManager.addRule(testTrigger2);
        actionsManager.addAction(lon_test2Spots);

        Trigger nextLight = new StateTrigger("rule.responsive_living_room_light", LightSimpleDevices.LIVING_SPOTS, "on", "Living spots are on");
        LightOffAction lof_chandelier = new LightOffAction("turn off living chandelier", LightDevices.LIVING_CHANDELIER);
        nextLight.addAction(lof_chandelier);
        rulesManager.addRule(nextLight);
        actionsManager.addAction(lof_chandelier);

        Trigger chandelierRule = new StateTrigger("rule.chandelier_light", LightDevices.LIVING_STANDING_LAMP, "off", "Living standing lamp is off");
        LightOnAction lon_chandelier = new LightOnAction("turn on living chandelier", LightDevices.LIVING_CHANDELIER, Color.CYAN, true);
        chandelierRule.addAction(lon_chandelier);
        rulesManager.addRule(chandelierRule);
        actionsManager.addAction(lon_chandelier);
    }
}
