package sven.phd.iot.models;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.rules.RulesManager;

public class StudyManager {
    private String ruleSet;
    private String deviceSet;
    private String stateSet;

    private ContextManager contextManager;

    public StudyManager(ContextManager contextManager) {
        System.out.println("StudyManager - Initiating...");
        this.contextManager = contextManager;
        this.setRuleSet("all");
        this.setDeviceSet("all");
        this.setStateSet("all");
    }

    public void setRuleSet(String ruleSet) {
        this.ruleSet = ruleSet;
        System.out.println("Rule set: " + ruleSet);

        RulesManager rulesManager = contextManager.getRulesManager();
        rulesManager.setAllRulesAvailable(false);

        if(ruleSet.equals("all")) {
            rulesManager.setAllRulesAvailable(true);
        } else if(ruleSet.equals("1")) {
            rulesManager.setRuleAvailable("rule.temp_average", true);
        }
    }

    public void setDeviceSet(String deviceSet) {
        System.out.println("Device set: " + deviceSet);
        this.deviceSet = deviceSet;

        HassioDeviceManager hassioDeviceManager = contextManager.getHassioDeviceManager();
        hassioDeviceManager.setAllDevicesAvailable(false);

        if(deviceSet.equals("all")) {
            hassioDeviceManager.setAllDevicesAvailable(true);
        } else if(deviceSet.equals("1")) {
            hassioDeviceManager.setDeviceAvailable("sensor.button_1_battery", true);
            hassioDeviceManager.setDeviceAvailable("sensor.button_2_battery", true);
            hassioDeviceManager.setDeviceAvailable("sensor.agoralaan_diepenbeek", true);
        } else if(deviceSet.equals("4")) {
            hassioDeviceManager.setDeviceAvailable("sun.sun", true);
        }
    }

    public void setStateSet(String stateSet) {
        System.out.println("State set: " + stateSet);
        this.stateSet = stateSet;
    }

    public String getRuleSet() {
        return ruleSet;
    }

    public String getDeviceSet() {
        return deviceSet;
    }

    public String getStateSet() {
        return stateSet;
    }
}
