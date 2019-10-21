package sven.phd.iot.rules;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.actions.LightOffAction;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.rules.triggers.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class RulesManager {
    private Map<String, Trigger> rules;

    public RulesManager() {
        System.out.println("Initiating rules...");
        this.rules = new HashMap<>();

        Trigger busyTrigger = new CalendarBusyTrigger("rule.sven_busy", "calendar.sven_coppers_uhasselt_be");
        busyTrigger.addAction(new LightOnAction("light.hue_color_spot_1", Color.YELLOW, false));
        this.rules.put("rule.busy", busyTrigger);

        Trigger availableTrigger = new CalendarAvailableTrigger("rule.sven_available", "calendar.sven_coppers_uhasselt_be");
        availableTrigger.addAction(new LightOffAction("light.hue_color_spot_1"));
        this.rules.put("rule.available", availableTrigger);

        Trigger sunSetTrigger = new SunSetTrigger("rule.sun_set");
        sunSetTrigger.addAction(new LightOnAction("light.hue_color_lamp_1", Color.YELLOW, false));
        sunSetTrigger.addAction(new LightOnAction("light.hue_color_lamp_2", Color.YELLOW, false));
        sunSetTrigger.addAction(new LightOnAction("light.hue_color_lamp_3", Color.YELLOW, false));
        this.rules.put("rule.sun_set", sunSetTrigger);

        Trigger sunRiseTrigger = new SunRiseTrigger("rule.sun_rise");
        sunRiseTrigger.addAction(new LightOffAction("light.hue_color_lamp_1"));
        sunRiseTrigger.addAction(new LightOffAction("light.hue_color_lamp_2"));
        sunRiseTrigger.addAction(new LightOffAction("light.hue_color_lamp_3"));
        this.rules.put("rule.sun_rise", sunRiseTrigger);

        Trigger coldTrigger = new TemperatureTrigger("rule.temp_cold", -50, 5);
        Trigger freshTrigger = new TemperatureTrigger("rule.temp_fresh",5, 10);
        Trigger averageTrigger = new TemperatureTrigger("rule.temp_average",10, 15);
        Trigger warmTrigger = new TemperatureTrigger("rule.temp_warm",15, 20);
        Trigger hotTrigger = new TemperatureTrigger("rule.temp_hot",20, 50);

        coldTrigger.addAction(new LightOnAction("light.hue_color_lamp_2", Color.BLUE, false));
        freshTrigger.addAction(new LightOnAction("light.hue_color_lamp_2", Color.GREEN, false));
        averageTrigger.addAction(new LightOnAction("light.hue_color_lamp_2", Color.YELLOW, false));
        warmTrigger.addAction(new LightOnAction("light.hue_color_lamp_2", Color.ORANGE, false));
        hotTrigger.addAction(new LightOnAction("light.hue_color_lamp_2", Color.RED, false));

        this.rules.put("rule.temp_cold", coldTrigger);
        this.rules.put("rule.temp_fresh", freshTrigger);
        this.rules.put("rule.temp_average", averageTrigger);
        this.rules.put("rule.temp_warm", warmTrigger);
        this.rules.put("rule.temp_hot", hotTrigger);

        Trigger weatherChangeTrigger = new WeatherChangeTrigger("rule.weather_change");
        weatherChangeTrigger.addAction(new LightOnAction("light.hue_color_lamp_3", Color.GREEN, false));
        this.rules.put("rule.weather_change", weatherChangeTrigger);
    }

    /**
     * Check when the rules will trigger
     * @param hassioStates the expected states at that time
     * @param hassioChange the change that caused the rules to be validated
     * @return
     */
    public List<HassioRuleExecutionEvent> verifyTriggers(HashMap<String, HassioState> hassioStates, HassioChange hassioChange) {
        List<HassioRuleExecutionEvent> triggerEvents = new ArrayList<>();

        for(String triggerName : this.rules.keySet()) {
            HassioRuleExecutionEvent newEvent = this.rules.get(triggerName).verify(hassioStates, hassioChange);

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

    public List<HassioRuleExecutionEvent> getFutureRuleExecutions() {
        List<HassioRuleExecutionEvent> executions = new ArrayList<>();

        for(String triggerName : this.rules.keySet()) {
            executions.addAll(this.rules.get(triggerName).getExecutionFuture());
        }

        Collections.sort(executions);

        return executions;
    }

    /**
     * Get the future states of each device
     * @return
     */
    public List<HassioRuleExecutionEvent> getFutureRuleExecutions(String id) {
        List<HassioRuleExecutionEvent> executions = new ArrayList<>();

        if(this.rules.containsKey(id)) {
            executions.addAll(this.rules.get(id).getExecutionFuture());
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

    public String printRulesToString() {
        String result = "";

        for(String triggerName : this.rules.keySet()) {
            result += this.rules.get(triggerName).toString() + "\n";
        }

        return result;
    }

    public void clearPredictions() {
        for(String triggerName : this.rules.keySet()) {
            this.rules.get(triggerName).clearPredictions();
        }
    }
}