package sven.phd.iot.study.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.rules.actions.StateAction;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.study.StudyRuleSet;

import java.awt.*;

public class BlindRules extends StudyRuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
        Trigger sunBelowHorizonTrigger = new StateTrigger("rule.sun_below_horizon_blinds", "sun.sun", "below_horizon", "sun set");
        sunBelowHorizonTrigger.addAction(new StateAction("lower the kitchen blinds", BlindDevices.KITCHEN_BLINDS, "lowered"));
        sunBelowHorizonTrigger.addAction(new StateAction("lower the living blinds", BlindDevices.LIVING_BLINDS, "lowered"));
        rulesManager.addRule(sunBelowHorizonTrigger);

        Trigger sunAboveHorizonTrigger = new StateTrigger("rule.sun_above_horizon_blinds", "sun.sun", "above_horizon", "sun rise");
        sunAboveHorizonTrigger.addAction(new StateAction("raise the kitchen blinds", BlindDevices.KITCHEN_BLINDS, "raised"));
        sunAboveHorizonTrigger.addAction(new StateAction("raise the living blinds", BlindDevices.LIVING_BLINDS, "raised"));
        rulesManager.addRule(sunAboveHorizonTrigger);

        Trigger rainTrigger = new StateTrigger("rule.weather_rain_lamps", "weather.dark_sky", "light_rain", "going to rain");
        rainTrigger.addAction(new LightOnAction("flash the living led strip purple", LightDevices.LIVING_LED_STRIPS, Color.PINK, true));
        rulesManager.addRule(rainTrigger);

        Trigger windyTrigger = new StateTrigger("rule.windy_blinds", "weather.dark_sky", "windy", "windy outside");
        windyTrigger.addAction(new StateAction("raise the kitchen blinds", BlindDevices.KITCHEN_BLINDS, "raised"));
        windyTrigger.addAction(new StateAction("raise the living blinds", BlindDevices.LIVING_BLINDS, "raised"));
        rulesManager.addRule(windyTrigger);
    }
}
