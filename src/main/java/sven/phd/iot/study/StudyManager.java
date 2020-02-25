package sven.phd.iot.study;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.climate.HassioCoolerAttributes;
import sven.phd.iot.hassio.climate.HassioHeaterAttributes;
import sven.phd.iot.hassio.climate.HassioThermostatAttributes;
import sven.phd.iot.hassio.light.HassioLightAttributes;
import sven.phd.iot.hassio.person.HassioPersonAttributes;
import sven.phd.iot.hassio.routine.HassioRoutineAttributes;
import sven.phd.iot.hassio.sensor.HassioBinarySensorAttributes;
import sven.phd.iot.hassio.sensor.HassioSensorAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.ThermostatStateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.study.cases.*;

import java.util.*;

public class StudyManager {
    private final HassioDeviceManager deviceManager;
    private final HassioStateScheduler stateScheduler;
    private final RulesManager rulesManager;
    private ContextManager contextManager;

    private HashMap<String, StudyDeviceSet> deviceSets;
    private HashMap<String, StudyRuleSet> ruleSets;
    private HashMap<String, StudyStateSet> stateSets;

    private List<String> activeDeviceSets;
    private List<String> activeRuleSets;
    private List<String> activeStateSets;

    public StudyManager(ContextManager contextManager) {
        System.out.println("StudyManager - Initiating...");
        this.contextManager = contextManager;
        this.deviceManager = contextManager.getHassioDeviceManager();
        this.stateScheduler = deviceManager.getStateScheduler();
        this.rulesManager = contextManager.getRulesManager();

        this.activeRuleSets = new ArrayList<>();
        this.ruleSets = new HashMap<>();
        this.ruleSets.put("children_temperature", new ChildrenTempRules());
        this.ruleSets.put("light_rules", new LightRules());
        this.ruleSets.put("living_temperature", new LivingTempRules());
        this.ruleSets.put("parent_temperature", new ParentTempRules());
        this.ruleSets.put("routine_rules", new WeekdayRoutineRules());
        this.ruleSets.put("shower_temperature", new ShowerTempRules());

        this.activeDeviceSets = new ArrayList<>();
        this.deviceSets = new HashMap<>();
        this.deviceSets.put("children_temperature", new ChildrenTempDevices());
        this.deviceSets.put("light_devices", new LightDevices());
        this.deviceSets.put("living_temperature", new LivingTempDevices());
        this.deviceSets.put("parent_temperature", new ParentTempDevices());
        this.deviceSets.put("routine_devices", new WeekdayRoutineDevices());
        this.deviceSets.put("shower_temperature", new ShowerTempDevices());
        this.deviceSets.put("virtual_sun", new VirtualSun());

        this.activeStateSets = new ArrayList<>();
        this.stateSets = new HashMap<>();
        this.stateSets.put("children_temperature", new ChildrenTempStates());
        this.stateSets.put("light_devices", new LightStates());
        this.stateSets.put("living_temperature", new LivingTempStates());
        this.stateSets.put("parent_temperature", new ParentTempStates());
        this.stateSets.put("routine_devices", new WeekDayRoutineStates());
        this.stateSets.put("shower_temperature", new ShowerTempStates());
        this.stateSets.put("virtual_sun_states", new VirtualSunStates());
    }

    public List<String> getRuleSet() {
        return this.activeRuleSets;
    }
    public List<String> getDeviceSet() {
        return this.activeDeviceSets;
    }
    public List<String> getStateSet() {
        return this.activeStateSets;
    }

    public List<String> getRuleSetOptions() { return new ArrayList<>(this.ruleSets.keySet()); }
    public List<String> getDeviceSetOptions() {
        return new ArrayList<>(this.deviceSets.keySet());
    }
    public List<String> getStateSetOptions() {
        return new ArrayList<>(this.stateSets.keySet());
    }

    public void setDeviceSet(List<String> deviceSets) {
        System.out.print("Device set: ");
        this.printStringList(deviceSets);

        this.activeDeviceSets.clear();

        this.deviceManager.setAllDevicesAvailable(false);
        ArrayList<HassioDevice> devices = new ArrayList<>();

        for(String deviceSet: deviceSets) {
            this.activeDeviceSets.add(deviceSet);
            this.deviceSets.get(deviceSet).createDevices(devices);
        }

        for(HassioDevice device: devices) {
            this.deviceManager.addDevice(device);
            device.setAvailable(true);
            device.setEnabled(true);
        }
    }

    public void setRuleSet(List<String> ruleSets) {
        System.out.print("Rule sets: ");
        this.printStringList(ruleSets);
        
        this.activeRuleSets.clear();

        this.rulesManager.setAllRulesAvailable(false);
        this.rulesManager.setAllRulesEnabled(false);

        for(String rulSet: ruleSets) {
            this.activeRuleSets.add(rulSet);
            this.ruleSets.get(rulSet).createRules(this.rulesManager);
        }
    }

    public void setStateSet(List<String> stateSets) {
        this.deviceManager.clearLogs();
        this.stateScheduler.clearScheduledStates();
        this.activeStateSets.clear();

        System.out.print("State sets: ");
        this.printStringList(stateSets);
        Calendar relativeTime = Calendar.getInstance();
        Date startDate = new Date();

        for(String stateSet: stateSets) {
            this.activeStateSets.add(stateSet);

            relativeTime.setTime(startDate);
            relativeTime.add(Calendar.MINUTE, -90); // Begin 90 minuten in het verleden

            this.stateSets.get(stateSet).setInitialStates(this.deviceManager, relativeTime.getTime());

            relativeTime.setTime(startDate);
            relativeTime.add(Calendar.MINUTE, 10); // 10 minuten in de toekomst

            this.stateSets.get(stateSet).scheduleFutureStates(this.stateScheduler, relativeTime);
        }

    }

    private void printStringList(List<String> stringlist) {
        if(stringlist.size() > 0) {
            System.out.print(stringlist.get(0));
        }

        for(int i = 1; i < stringlist.size(); i++) {
            System.out.print(", " + stringlist.get(i));
        }

        System.out.println();
    }
}
