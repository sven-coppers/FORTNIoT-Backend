package sven.phd.iot.rules;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioAbstractState;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.actions.LightOffAction;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.rules.actions.OutletAction;
import sven.phd.iot.rules.actions.StateAction;
import sven.phd.iot.rules.triggers.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class RulesManager {
    private Map<String, Trigger> rules;

    public RulesManager() {
        System.out.println("RulesManager - Initiating...");
        this.rules = new HashMap<>();

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

        Trigger minimumTempTrigger = new TemperatureTrigger("rule.mininum_temperature", -50, 15);
        minimumTempTrigger.addAction(new StateAction("turn on the heating", "heater.heater", "heating"));
        this.rules.put("rule.mininum_temperature", minimumTempTrigger);

        Trigger heaterTrigger = new StateTrigger("rule.heater_on_trigger", "heater.heater", "heating", "If the heater is on");
        heaterTrigger.addAction(new LightOffAction("turn off standing lamp", "light.standing_lamp"));
        this.rules.put("rule.heater_on_trigger", heaterTrigger);


        //Load Bram's rules
        //this.rules.putAll(BramRulesManager.getRules());

    }

    /**
     * Check when the rules will trigger
     * @param hassioStates the expected states at that time
     * @param hassioChange the change that caused the rules to be validated
     * @return
     */
    public List<HassioRuleExecutionEvent> verifyTriggers(HashMap<String, HassioState> hassioStates, HassioChange hassioChange) {
        return this.verifyTriggers(hassioStates, hassioChange, new HashMap<>());
    }

    /**
     * Check when the rules will trigger
     * @param hassioStates the expected states at that time
     * @param hassioChange the change that caused the rules to be validated
     * @param simulatedRulesEnabled simulate whether some rules are enbaled/disabled
     * @return
     */
    public List<HassioRuleExecutionEvent> verifyTriggers(HashMap<String, HassioState> hassioStates, HassioChange hassioChange, HashMap<String, Boolean> simulatedRulesEnabled) {
        List<HassioRuleExecutionEvent> triggerEvents = new ArrayList<>();

        for(String triggerName : this.rules.keySet()) {
            boolean enabled = this.rules.get(triggerName).enabled;

            if(simulatedRulesEnabled.containsKey(triggerName)) {
                enabled = simulatedRulesEnabled.get(triggerName);
            }

            HassioRuleExecutionEvent newEvent = this.rules.get(triggerName).verify(hassioStates, hassioChange, enabled);

            if(newEvent != null) {
                triggerEvents.add(newEvent);
            }
        }

        return triggerEvents;
    }

    public List<HassioRuleExecutionEvent> getPastRuleExecutions() {
        List<HassioRuleExecutionEvent> executions = new ArrayList<>();

        for(String triggerName : this.rules.keySet()) {
            executions.addAll(this.rules.get(triggerName).getExecutionHistory());
        }

        Collections.sort(executions);

        return executions;
    }

    /**
     * Get the history states of each device
     * @return
     */
    public List<HassioRuleExecutionEvent> getPastRuleExecutions(String id) {
        List<HassioRuleExecutionEvent> executions = new ArrayList<>();

        if(this.rules.containsKey(id)) {
            executions.addAll(this.rules.get(id).getExecutionHistory());
        }

        Collections.sort(executions);

        return executions;
    }

    public Trigger getRule(String id) {
        return this.rules.get(id);
    }

    public String printRulesToString() {
        String result = "";

        for(String triggerName : this.rules.keySet()) {
            result += this.rules.get(triggerName).toString() + "\n";
        }

        return result;
    }

    public Map<String, Trigger> getRules() {
        return this.rules;
    }

    public Trigger getRuleById(String id) {
        return rules.get(id);
    }

    public void setAllRulesAvailable(boolean available) {
        for(String deviceID : this.rules.keySet()) {
            rules.get(deviceID).setAvailable(available);
        }
    }

    public void setRuleEnabled(String ruleID, boolean enabled) {
        if(rules.containsKey(ruleID)) {
            rules.get(ruleID).setEnabled(enabled);
        }
    }

    public void setRuleAvailable(String ruleID, boolean available) {
        if(rules.containsKey(ruleID)) {
            rules.get(ruleID).setAvailable(available);

            // It would be confusing if a rule is unavailable to the user, but still active
            if(available == false) {
                this.setRuleEnabled(ruleID, false);
            }
        }
    }
}