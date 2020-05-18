package sven.phd.iot.study;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.students.mathias.ActionsManager;
import sven.phd.iot.study.cases.*;

import java.util.*;

public class StudyManager {
    private final HassioDeviceManager deviceManager;
    private final HassioStateScheduler stateScheduler;
    private final RulesManager rulesManager;
    private final ActionsManager actionsManager;
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
        // MATHIAS
        this.actionsManager = contextManager.getActionsManager();

        this.activeRuleSets = new ArrayList<>();
        this.ruleSets = new HashMap<>();
        this.ruleSets.put("children_temperature", new ChildrenTempRules());
        this.ruleSets.put("light_rules", new LightRules());
        this.ruleSets.put("living_temperature", new LivingTempRules());
        this.ruleSets.put("parent_temperature", new ParentTempRules());
        this.ruleSets.put("routine_rules", new RoutineRules());
        this.ruleSets.put("shower_temperature", new ShowerTempRules());
        this.ruleSets.put("tv_rules", new TVRules());
        this.ruleSets.put("security_rules", new SecurityRules());
        this.ruleSets.put("cleaning_rules", new CleaningRules());
        this.ruleSets.put("bram_rules_1", new BramRuleSet_1());
        this.ruleSets.put("cleaning_start", new CleaningRules());
        this.ruleSets.put("cleaning_stop", new CleaningStopRules());
        this.ruleSets.put("blind_rules", new BlindRules());
        this.ruleSets.put("light_simple", new LightSimpleRules());
        this.ruleSets.put("smoke", new SmokeRules());
        this.ruleSets.put("smoke_advanced", new SmokeAdvancedRules());

        this.activeDeviceSets = new ArrayList<>();
        this.deviceSets = new HashMap<>();
        this.deviceSets.put("children_temperature", new ChildrenTempDevices());
        this.deviceSets.put("light_devices", new LightDevices());
        this.deviceSets.put("living_temperature", new LivingTempDevices());
        this.deviceSets.put("parent_temperature", new ParentTempDevices());
        this.deviceSets.put("routine_devices", new RoutineDevices());
        this.deviceSets.put("shower_temperature", new ShowerTempDevices());
        this.deviceSets.put("sun", new VirtualSun());
        this.deviceSets.put("tv_devices", new TVDevices());
        this.deviceSets.put("security_devices", new SecurityDevices());
        this.deviceSets.put("cleaning_devices", new CleaningDevices());
        this.deviceSets.put("bram_devices_1", new BramDeviceSet_1());
        this.deviceSets.put("blind_devices", new BlindDevices());
        this.deviceSets.put("weather", new WeatherDevices());
        this.deviceSets.put("light_simple", new LightSimpleDevices());
        this.deviceSets.put("smoke", new SmokeDevices());


        this.activeStateSets = new ArrayList<>();
        this.stateSets = new HashMap<>();
        this.stateSets.put("children_temperature", new ChildrenTempStates());
        this.stateSets.put("light_off", new LightStates());
        this.stateSets.put("light_on", new LightStatesOn());
        this.stateSets.put("living_temperature_off", new LivingTempStates());
        this.stateSets.put("living_temperature_on", new LivingTempOnStates());
        this.stateSets.put("parent_temperature", new ParentTempStates());
        this.stateSets.put("routine_workday", new RoutineWorkingStates());
        this.stateSets.put("routine_weekend", new RoutineWeekendStates());
        this.stateSets.put("routine_home", new RoutineHomeStates());
        this.stateSets.put("shower_temperature", new ShowerTempStates());
        this.stateSets.put("sun_day_night_day", new VirtualSunStates());
        this.stateSets.put("sun_night_day_night", new VirtualSunStatesNight());
        this.stateSets.put("tv_news", new TVNewsStates());
        this.stateSets.put("tv_sports", new TVSportsStates());
        this.stateSets.put("tv_sports_late", new TVSportsLateStates());
        this.stateSets.put("tv_movies", new TVMovieStates());
        this.stateSets.put("security_states", new SecurityStates());
        this.stateSets.put("cleaning_states", new CleaningStates());
        this.stateSets.put("bram_states_1", new BramStateSet_1());



        this.stateSets.put("cleaning_idle", new CleaningStates());
        this.stateSets.put("cleaning_ongoing", new CleaningOngoingStates());
        this.stateSets.put("blind_states", new BlindStates());
        this.stateSets.put("weather_rain_states", new WeatherRainStates());
        this.stateSets.put("weather_clear_states", new WeatherClearStates());
        this.stateSets.put("weather_windy_states", new WeatherWindyStates());
        this.stateSets.put("light_simple", new LightSimpleStates());
        this.stateSets.put("smoke_idle", new SmokeIdleStates());
        this.stateSets.put("smoke_smoke", new SmokeSmokeStates());

        // MATHIAS ADDING STUDY
        ArrayList<String> rules = new ArrayList<>();
        rules.add("light_rules");

        ArrayList<String> devices = new ArrayList<>();
        devices.add("light_devices");
        devices.add("light_simple");
        devices.add("sun");

        ArrayList<String> states = new ArrayList<>();
        states.add("light_off");
        states.add("light_on");
        states.add("light_simple");
        states.add("sun_day_night_day");
        states.add("sun_night_day_night");

        setRuleSet(rules);
        setDeviceSet(devices);
        setStateSet(states);
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
            this.ruleSets.get(rulSet).createRules(this.rulesManager, this.actionsManager);
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
