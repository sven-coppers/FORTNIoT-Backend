package sven.phd.iot.study.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.LightOffAction;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.rules.actions.ThermostatStateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.students.bram.rules.triggers.ANDTrigger;
import sven.phd.iot.study.StudyRuleSet;

import java.awt.*;

public class LightRules extends StudyRuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
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

        Trigger rainTrigger = new StateTrigger("rule.weather_rain_lamps", "weather.dark_sky", "light_rain", "going to rain");
        rainTrigger.addAction(new LightOnAction("flash the living led strip purple", LightDevices.LIVING_LED_STRIPS, Color.PINK, true));
        rulesManager.addRule(rainTrigger);

        // Mathias test trigger
        Trigger testTrigger = new StateTrigger("rule.test_lights", "sun.sun", "above_horizon", "TEST sun rises");
        testTrigger.addAction(new LightOffAction("turn off living spots", LightSimpleDevices.LIVING_SPOTS));
        rulesManager.addRule(testTrigger);
    }
}
