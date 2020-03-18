package sven.phd.iot.hassio.updates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ImplicitBehaviorEvent extends HassioRuleExecutionEvent {
    @JsonIgnore private List<String> triggerDevicesIDs;
    @JsonIgnore private List<String> actionDevicesIDs;
    @JsonIgnore private List<HassioState> actionStates;

    public ImplicitBehaviorEvent(String ruleID, Date datetime) {
        super(ruleID, datetime);
        triggerDevicesIDs = new ArrayList<>();
        actionDevicesIDs = new ArrayList<>();
        actionStates = new ArrayList<>();
    }

    public ImplicitBehaviorEvent(Date datetime) {
        super(RulesManager.RULE_IMPLICIT_BEHAVIOR, datetime);
        triggerDevicesIDs = new ArrayList<>();
        actionDevicesIDs = new ArrayList<>();
        actionStates = new ArrayList<>();
    }

    /**
     * Constructor meant for devices that update themselves
     * @param datetime
     * @param actionDeviceID
     */
    public ImplicitBehaviorEvent(Date datetime, String actionDeviceID) {
        super(RulesManager.RULE_IMPLICIT_BEHAVIOR, datetime);
        triggerDevicesIDs = new ArrayList<>();
        actionDevicesIDs = new ArrayList<>();
        actionStates = new ArrayList<>();

        this.addActionDeviceID(actionDeviceID);
        this.addTriggerDeviceID(actionDeviceID);
    }

    public void addTriggerDeviceID(String triggerDevice) {
        this.triggerDevicesIDs.add(triggerDevice);
    }

    public void addActionDeviceID(String actionDevice) {
        this.actionDevicesIDs.add(actionDevice);
    }

    public List<String> getTriggerDeviceIDs() {
        return this.triggerDevicesIDs;
    }

    public List<String> getActionDeviceIDs() {
        return this.actionDevicesIDs;
    }

    public void resolveContextIDs(HashMap<String, HassioState> hassioStates) {
        for(String deviceID: this.triggerDevicesIDs) {
            this.triggerContexts.add(hassioStates.get(deviceID).context);
        }

        for(String deviceID: this.actionDevicesIDs) {
            this.actionContexts.add(hassioStates.get(deviceID).context);
            this.actionStates.add(hassioStates.get(deviceID));
        }
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public List<HassioState> getActionStates() {
        return this.actionStates;
    }
}
