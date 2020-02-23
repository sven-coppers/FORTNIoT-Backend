package sven.phd.iot.study;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.rules.RulesManager;

public class StudyManager {
    private StudyDevices studyDevices;
    private StudyRules studyRules;
    private StudyStates studyStates;

    private ContextManager contextManager;

    public StudyManager(ContextManager contextManager) {
        System.out.println("StudyManager - Initiating...");
        this.studyRules = new StudyRules(contextManager.getRulesManager());
        this.studyDevices = new StudyDevices(contextManager.getHassioDeviceManager());
        this.studyStates = new StudyStates(contextManager.getHassioDeviceManager());
        this.contextManager = contextManager;
    }

    public String getRuleSet() {
        return this.studyRules.getRuleSet();
    }
    public String getDeviceSet() {
        return this.studyDevices.getDeviceSet();
    }
    public String getStateSet() {
        return this.studyStates.getStateSet();
    }

    public void setRuleSet(String ruleSet) { this.studyRules.setRuleSet(ruleSet); }
    public void setDeviceSet(String ruleSet) { this.studyDevices.setDeviceSet(ruleSet); }
    public void setStateSet(String ruleSet) { this.studyStates.setStateSet(ruleSet); }
}
