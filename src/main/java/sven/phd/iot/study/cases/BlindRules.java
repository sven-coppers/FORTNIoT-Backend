package sven.phd.iot.study.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.rules.actions.StateAction;
import sven.phd.iot.rules.triggers.SensorReachesTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.study.StudyRuleSet;

import java.awt.*;

public class BlindRules extends StudyRuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
        Trigger sunBelowHorizonTrigger = new StateTrigger("rule.sun_below_horizon_blinds", "sun.sun", "below_horizon", "sun set");
        sunBelowHorizonTrigger.addAction(new StateAction("lower the rolling shutter", BlindDevices.LIVING_BLINDS, "lowered"));
        rulesManager.addRule(sunBelowHorizonTrigger);

        Trigger sunAboveHorizonTrigger = new StateTrigger("rule.sun_above_horizon_blinds", "sun.sun", "above_horizon", "sun rise");
        sunAboveHorizonTrigger.addAction(new StateAction("raise the rolling shutter", BlindDevices.LIVING_BLINDS, "raised"));
        rulesManager.addRule(sunAboveHorizonTrigger);

        Trigger windyTrigger = new SensorReachesTrigger("system_rule.windy_blinds", WeatherDevices.WEATHER_WIND_SPEED, 50, false, "wind speed", "KM/H");
        windyTrigger.addAction(new StateAction("", BlindDevices.LIVING_BLINDS, "lowered"));
        rulesManager.addRule(windyTrigger);
        windyTrigger.setTitle("IF wind speed reaches 50KM/H THEN lower the rolling shutter");

        Trigger toWarmTrigger = new SensorReachesTrigger("rule.too_warm_outside", WeatherDevices.WEATHER_TEMPERATURE, 25, false, "outdoor temperature", "°C");
        toWarmTrigger.addAction(new StateAction("lower the rolling shutter", BlindDevices.LIVING_BLINDS, "lowered"));
        rulesManager.addRule(toWarmTrigger);
    }
}
