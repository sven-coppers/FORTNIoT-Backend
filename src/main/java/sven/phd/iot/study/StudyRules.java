package sven.phd.iot.study;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.StateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.rules.triggers.TemperatureReachesTrigger;
import sven.phd.iot.students.bram.rules.triggers.ANDTrigger;

public class StudyRules {
    private RulesManager rulesManager;
    private String ruleSet;

    public StudyRules(RulesManager rulesManager) {
        this.rulesManager = rulesManager;
        this.initTempRules();
        this.setRuleSet("all");
    }

    public String getRuleSet() {
        return ruleSet;
    }
    public void setRuleSet(String ruleSet) {
        this.ruleSet = ruleSet;
        System.out.println("Rule set: " + ruleSet);

        this.rulesManager.setAllRulesAvailable(false);

        if(ruleSet.equals("all")) {
            this.rulesManager.setAllRulesAvailable(true);
        } else {
            this.rulesManager.setAllRulesAvailable(false);

            if(ruleSet.equals("1")) {
                this.rulesManager.setRuleAvailable("rule.mininum_temperature", true);
                this.rulesManager.setRuleAvailable("rule.mininum_temperature_end", true);
                this.rulesManager.setRuleAvailable("rule.mininum_temperature_occupied", true);
                this.rulesManager.setRuleAvailable("rule.mininum_temperature_occupied_end", true);
            }
        }
    }

    private void initTempRules() {
        ANDTrigger minimumTempTrigger = new ANDTrigger("rule.mininum_temperature");
        minimumTempTrigger.addTrigger(new TemperatureReachesTrigger("", "sensor.indoor_temperature_measurement", 15, true));
        minimumTempTrigger.addTrigger(new PeopleHomeTrigger("", false));
        minimumTempTrigger.addAction(new StateAction("set the heating to ECO", "heater.heater", "eco"));
        this.rulesManager.addRule(minimumTempTrigger);

        ANDTrigger minimumTempEndTrigger = new ANDTrigger("rule.mininum_temperature_end");
        minimumTempEndTrigger.addTrigger(new TemperatureReachesTrigger("", "sensor.indoor_temperature_measurement", 16, false));
        minimumTempEndTrigger.addTrigger(new PeopleHomeTrigger("", false));
        minimumTempEndTrigger.addAction(new StateAction("turn off the heating", "heater.heater", "off"));
        this.rulesManager.addRule(minimumTempEndTrigger);

        ANDTrigger minimumTempOccupiedTrigger = new ANDTrigger("rule.mininum_temperature_occupied");
        minimumTempOccupiedTrigger.addTrigger(new TemperatureReachesTrigger("", "sensor.indoor_temperature_measurement", 20, true));
        minimumTempOccupiedTrigger.addTrigger(new PeopleHomeTrigger("", true));
        minimumTempOccupiedTrigger.addAction(new StateAction("start heating", "heater.heater", "heating"));
        this.rulesManager.addRule(minimumTempOccupiedTrigger);

        ANDTrigger minimumTempOccupiedEndTrigger = new ANDTrigger("rule.mininum_temperature_occupied_end");
        minimumTempOccupiedEndTrigger.addTrigger(new TemperatureReachesTrigger("", "sensor.indoor_temperature_measurement", 21, false));
        minimumTempOccupiedEndTrigger.addTrigger(new PeopleHomeTrigger("", true));
        minimumTempOccupiedEndTrigger.addAction(new StateAction("turn off the heating", "heater.heater", "off"));
        this.rulesManager.addRule(minimumTempOccupiedEndTrigger);

    }

    /*         Trigger busyTrigger = new StateTrigger("rule.sven_busy", "calendar.sven_coppers_uhasselt_be", "on", "WHEN Sven is busy");
        busyTrigger.addAction(new LightOnAction("turn Spot 3 Yellow", "light.hue_color_spot_3", Color.YELLOW, false));
        this.rules.put("rule.sven_busy", busyTrigger);

        Trigger availableTrigger = new StateTrigger("rule.sven_available", "calendar.sven_coppers_uhasselt_be", "off", "WHEN Sven is available");
        availableTrigger.addAction(new LightOffAction("turn off Spot 3","light.hue_color_spot_3"));
        this.rules.put("rule.sven_available", availableTrigger);

        Trigger sunSetTrigger = new StateTrigger("rule.sun_set", "sun.sun", "below_horizon", "IF sun set");
        sunSetTrigger.addAction(new LightOnAction("turn Lamp 1", "light.hue_color_lamp_1", Color.YELLOW, false));
        sunSetTrigger.addAction(new LightOnAction("Lamp 2", "light.hue_color_lamp_2", Color.YELLOW, false));
        sunSetTrigger.addAction(new LightOnAction("Lamp 3 Yellow", "light.hue_color_lamp_3", Color.YELLOW, false));
        this.rules.put("rule.sun_set", sunSetTrigger);

        Trigger sunRiseTrigger = new StateTrigger("rule.sun_rise", "sun.sun", "above_horizon", "IF sun rise");
        sunRiseTrigger.addAction(new LightOffAction("turn off Lamp 1","light.hue_color_lamp_1"));
        sunRiseTrigger.addAction(new LightOffAction("Lamp 2","light.hue_color_lamp_2"));
        sunRiseTrigger.addAction(new LightOffAction("Lamp 3","light.hue_color_lamp_3"));
        this.rules.put("rule.sun_rise", sunRiseTrigger);

        Trigger coldTrigger = new TemperatureTrigger("rule.temp_cold", -50, 5);
        Trigger freshTrigger = new TemperatureTrigger("rule.temp_fresh",5, 10);
        Trigger averageTrigger = new TemperatureTrigger("rule.temp_average",10, 15);
        Trigger warmTrigger = new TemperatureTrigger("rule.temp_warm",15, 20);
        Trigger hotTrigger = new TemperatureTrigger("rule.temp_hot",20, 50);

        coldTrigger.addAction(new LightOnAction("turn Lamp 2 Blue", "light.hue_color_lamp_2", Color.BLUE, false));
        freshTrigger.addAction(new LightOnAction("turn Lamp 2 Green)", "light.hue_color_lamp_2", Color.GREEN, false));
        averageTrigger.addAction(new LightOnAction("turn Lamp 2 Yellow", "light.hue_color_lamp_2", Color.YELLOW, false));
        warmTrigger.addAction(new LightOnAction("turn Lamp 2 Orange", "light.hue_color_lamp_2", Color.ORANGE, false));
        hotTrigger.addAction(new LightOnAction("turn Lamp 2 Red", "light.hue_color_lamp_2", Color.RED, false));

        this.rules.put("rule.temp_cold", coldTrigger);
        this.rules.put("rule.temp_fresh", freshTrigger);
        this.rules.put("rule.temp_average", averageTrigger);
        this.rules.put("rule.temp_warm", warmTrigger);
        this.rules.put("rule.temp_hot", hotTrigger);

        Trigger weatherChangeTrigger = new WeatherChangeTrigger("rule.weather_change");
        weatherChangeTrigger.addAction(new LightOnAction("turn Lamp 3 Green", "light.hue_color_lamp_3", Color.GREEN, false));
        this.rules.put("rule.weather_change", weatherChangeTrigger);

        Trigger motionTrigger = new StateTrigger("rule.motion_detected","binary_sensor.motion_sensor_motion", "on", "If motion detected");
        motionTrigger.addAction(new LightOnAction("turn Spot 1 Purple", "light.hue_color_spot_1", Color.magenta, false));
        motionTrigger.addAction(new OutletAction("turn on Outlet 3", "switch.outlet_3", "on"));
        this.rules.put("rule.motion_detected", motionTrigger);

        Trigger noMotionTrigger = new StateTrigger("rule.motion_clear","binary_sensor.motion_sensor_motion", "off", "If no motion");
        noMotionTrigger.addAction(new LightOffAction("turn off Spot 1", "light.hue_color_spot_1"));
        noMotionTrigger.addAction(new OutletAction("turn off Outlet 3", "switch.outlet_3", "off"));
        this.rules.put("rule.motion_clear", noMotionTrigger);




        //Trigger somebodyHomeTrigger = new Person

        Trigger heaterTrigger = new StateTrigger("rule.heater_on_trigger", "heater.heater", "heating", "If the heater is on");
        heaterTrigger.addAction(new LightOffAction("turn off standing lamp", "light.standing_lamp"));
        this.rules.put("rule.heater_on_trigger", heaterTrigger); */
}
