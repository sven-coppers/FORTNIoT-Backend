package sven.phd.iot.study.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.ThermostatStateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.study.StudyRuleSet;

public class ChildrenTempRules extends StudyRuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
        Trigger childrenMovingTrigger = new StateTrigger("rule.children_moving", ChildrenTempDevices.BEDROOM_CHILDREN_MOTION, "moving", "movement in children's room");
        childrenMovingTrigger.addAction(new ThermostatStateAction(ChildrenTempDevices.BEDROOM_CHILDREN_THERMOSTAT, "children's room thermostat", 21.0));
        rulesManager.addRule(childrenMovingTrigger);

        Trigger childrenStopMovingTrigger = new StateTrigger("rule.children_stop_moving", ChildrenTempDevices.BEDROOM_CHILDREN_MOTION, "clear", "no more movement in children's room");
        childrenStopMovingTrigger.addAction(new ThermostatStateAction(ChildrenTempDevices.BEDROOM_CHILDREN_THERMOSTAT, "children's room thermostat", 15.0));
        rulesManager.addRule(childrenStopMovingTrigger);

        Trigger nobodyHomeTrigger = new PeopleHomeTrigger("rule.nobody_home_children", false);
        nobodyHomeTrigger.addAction(new ThermostatStateAction(ChildrenTempDevices.BEDROOM_CHILDREN_THERMOSTAT, "children's bedroom thermostat", 15.0));
        rulesManager.addRule(nobodyHomeTrigger);

        Trigger eveningTrigger = new StateTrigger("rule.evening_children", RoutineDevices.ROUTINE, "evening", "evening");
        eveningTrigger.addAction(new ThermostatStateAction(ChildrenTempDevices.BEDROOM_CHILDREN_THERMOSTAT, "children's bedroom thermostat", 20.0));
        rulesManager.addRule(eveningTrigger);

        Trigger sleepingTrigger = new StateTrigger("rule.sleeping_children", RoutineDevices.ROUTINE, "sleeping", "everyone is sleeping");
        sleepingTrigger.addAction(new ThermostatStateAction(ChildrenTempDevices.BEDROOM_CHILDREN_THERMOSTAT, "children's bedroom thermostat", 19.0));
        rulesManager.addRule(sleepingTrigger);
    }
}


    /*
            // this.rulesManager.addRule(morningTrigger);

    //  ANDTrigger tempComfortTrigger = new ANDTrigger("rule.temperature_comfort");
    //  tempComfortTrigger.addTrigger(new PeopleHomeTrigger("", true));
    // tempComfortTrigger.addAction(new ThermostatStateAction("heater.heater", "heating", 21.0));
    // this.rulesManager.addRule(tempComfortTrigger);

     Trigger busyTrigger = new StateTrigger("rule.sven_busy", "calendar.sven_coppers_uhasselt_be", "on", "WHEN Sven is busy");
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
