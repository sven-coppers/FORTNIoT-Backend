package sven.phd.iot.scenarios;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.scenarios.cases.*;

import java.util.*;

public class ScenarioManager {
    private final HassioDeviceManager deviceManager;
    private final HassioStateScheduler stateScheduler;
    private final RulesManager rulesManager;
    private ContextManager contextManager;

    private HashMap<String, DeviceSet> deviceSets;
    private HashMap<String, RuleSet> ruleSets;
    private HashMap<String, StateSet> stateSets;
    private HashMap<String, Preset> presets;

    private List<String> activeDeviceSets;
    private List<String> activeRuleSets;
    private List<String> activeStateSets;
    private String activePreset;

    public ScenarioManager(ContextManager contextManager) {
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
        this.ruleSets.put("routine_rules", new RoutineRules());
        this.ruleSets.put("shower_temperature", new ShowerTempRules());
        this.ruleSets.put("tv_rules", new TVRules());
        this.ruleSets.put("security_rules", new SecurityRules());
        this.ruleSets.put("cleaning_start", new CleaningRules());
        this.ruleSets.put("cleaning_stop", new CleaningStopRules());
        this.ruleSets.put("blind_rules", new BlindRules());
        this.ruleSets.put("light_simple", new LightSimpleRules());
        this.ruleSets.put("light_simple_conflict", new LightSimpleRulesInconsistency());
        this.ruleSets.put("smoke", new SmokeRules());
        this.ruleSets.put("smoke_advanced", new SmokeAdvancedRules());
        this.ruleSets.put("race_condition", new RaceConditionRules());
        this.ruleSets.put("inconsistency", new InconsistencyRules());
        this.ruleSets.put("inconsistency_uc1", new InconsistencyRules1());
        this.ruleSets.put("inconsistency_uc2", new InconsistencyRules2());
        this.ruleSets.put("loops", new LoopyRules());
        this.ruleSets.put("loops_uc1", new LoopyRules1());
        this.ruleSets.put("redundancy_uc1", new RedundancyRules1());
        this.ruleSets.put("redundancy_uc2", new RedundancyRules2());
        this.ruleSets.put("bedroom_temperature_rules_conflict", new BedroomTempRulesConflict());

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
        this.deviceSets.put("blind_devices", new BlindDevices());
        this.deviceSets.put("weather", new WeatherDevices());
        this.deviceSets.put("light_simple", new LightSimpleDevices());
        this.deviceSets.put("smoke", new SmokeDevices());
        this.deviceSets.put("race_condition", new RaceConditionDevices());
        this.deviceSets.put("inconsistency", new InconsistencyDevices());
        this.deviceSets.put("loops", new LoopyDevices());
        this.deviceSets.put("redundancy", new RedundancyDevices());
        this.deviceSets.put("bedroom_temperature", new BedroomTempDevices());

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
        this.stateSets.put("routine_weekend_late", new RoutineWeekendLateStates());
        this.stateSets.put("routine_home", new RoutineHomeStates());
        this.stateSets.put("routine_sleeping", new RoutineSleepingStates());
        this.stateSets.put("shower_temperature", new ShowerTempStates());
        this.stateSets.put("sun_day_night_day", new VirtualSunStates());
        this.stateSets.put("sun_night_day_night", new VirtualSunStatesNight());
        this.stateSets.put("tv_news", new TVNewsStates());
        this.stateSets.put("tv_sports", new TVSportsStates());
        this.stateSets.put("tv_sports_late", new TVSportsLateStates());
        this.stateSets.put("tv_movies", new TVMovieStates());
        this.stateSets.put("security_states", new SecurityStates());
        this.stateSets.put("cleaning_idle", new CleaningStates());
        this.stateSets.put("cleaning_ongoing", new CleaningOngoingStates());
        this.stateSets.put("blind_states", new BlindStates());
        this.stateSets.put("weather_rain_states", new WeatherRainStates());
        this.stateSets.put("weather_clear_states", new WeatherClearStates());
        this.stateSets.put("weather_windy_states", new WeatherWindyStates());
        this.stateSets.put("light_simple", new LightSimpleStates());
        this.stateSets.put("smoke_idle", new SmokeIdleStates());
        this.stateSets.put("smoke_smoke", new SmokeSmokeStates());
        this.stateSets.put("teaser", new TeaserStates());
        this.stateSets.put("race_condition", new RaceConditionStates());
        this.stateSets.put("race_condition_2_min", new RaceConditionStatesDelayed());
        this.stateSets.put("inconsistency", new InconsistencyStates());
        this.stateSets.put("inconsistency_uc1", new InconsistencyStates1());
        this.stateSets.put("inconsistency_uc2", new InconsistencyStates2());
        this.stateSets.put("loops", new LoopyStates());
        this.stateSets.put("loops_uc1", new LoopyStates1());
        this.stateSets.put("redundancy_uc1", new RedundancyStates1());
        this.stateSets.put("redundancy_uc2", new RedundancyStates2());
        this.stateSets.put("bedroom_temperature_summer", new BedroomTempStatesSummer());

        // MATHIAS SETUP
        this.activeRuleSets.add("light_rules");
        this.activeDeviceSets.add("light_devices");
        this.activeDeviceSets.add("sun");
        this.activeStateSets.add("light_off");
        this.activeStateSets.add("light_on");
        this.activeStateSets.add("light_simple");
        this.activeStateSets.add("sun_day_night_day");
        this.activeStateSets.add("sun_night_day_night");

        this.presets = new HashMap<>();
        //                              Preset(List<String> newDeviceSets, List<String> newStateSets, List<String> newRuleSets, List<String> conflictVerifiers)
        this.presets.put("tr_v1", new Preset(Arrays.asList("light_simple", "smoke", "sun"), Arrays.asList("light_simple", "smoke_idle", "sun_day_night_day"), Arrays.asList("light_simple", "smoke"), Arrays.asList()));
        this.presets.put("tr_v2", new Preset(Arrays.asList("light_simple", "smoke", "sun"), Arrays.asList("light_simple", "smoke_smoke", "sun_night_day_night"), Arrays.asList("light_simple", "smoke"), Arrays.asList()));
        this.presets.put("tr_v3", new Preset(Arrays.asList("light_simple", "smoke", "sun"), Arrays.asList("light_simple", "smoke_idle", "sun_night_day_night"), Arrays.asList("light_simple", "smoke"), Arrays.asList()));
        this.presets.put("tr_v4", new Preset(Arrays.asList("light_simple", "smoke", "sun"), Arrays.asList("light_simple", "smoke_smoke", "sun_day_night_day"), Arrays.asList("light_simple", "smoke"), Arrays.asList()));
        this.presets.put("uc1_v1", new Preset(Arrays.asList("light_devices", "routine_devices", "tv_devices"), Arrays.asList("light_off", "routine_workday", "tv_news"), Arrays.asList("tv_rules"), Arrays.asList()));
        this.presets.put("uc1_v2", new Preset(Arrays.asList("light_devices", "routine_devices", "tv_devices"), Arrays.asList("light_off", "routine_workday", "tv_sports"), Arrays.asList("tv_rules"), Arrays.asList()));
        this.presets.put("uc1_v3", new Preset(Arrays.asList("light_devices", "routine_devices", "tv_devices"), Arrays.asList("light_on", "routine_home", "tv_news"), Arrays.asList("tv_rules"), Arrays.asList()));
        this.presets.put("uc1_v4", new Preset(Arrays.asList("light_devices", "routine_devices", "tv_devices"), Arrays.asList("light_on", "routine_weekend", "tv_sports_late"), Arrays.asList("tv_rules"), Arrays.asList()));
        this.presets.put("uc2_v1", new Preset(Arrays.asList("living_temperature", "routine_devices"), Arrays.asList("living_temperature_off", "routine_workday"), Arrays.asList("living_temperature"), Arrays.asList()));
        this.presets.put("uc2_v2", new Preset(Arrays.asList("living_temperature", "routine_devices"), Arrays.asList("living_temperature_off", "routine_workday"), Arrays.asList("living_temperature"), Arrays.asList()));
        this.presets.put("uc2_v3", new Preset(Arrays.asList("living_temperature", "routine_devices"), Arrays.asList("living_temperature_on", "routine_weekend"), Arrays.asList("living_temperature"), Arrays.asList()));
        this.presets.put("uc2_v4", new Preset(Arrays.asList("living_temperature", "routine_devices"), Arrays.asList("living_temperature_on", "routine_weekend"), Arrays.asList("living_temperature"), Arrays.asList()));
        this.presets.put("uc3_v1", new Preset(Arrays.asList("blind_devices", "light_devices", "routine_devices", "sun", "weather"), Arrays.asList("blind_states", "light_off", "sun_day_night_day", "weather_clear_states", "routine_home"), Arrays.asList("blind_rules", "light_rules"), Arrays.asList()));
        this.presets.put("uc3_v2", new Preset(Arrays.asList("blind_devices", "light_devices", "routine_devices", "sun", "weather"), Arrays.asList("blind_states", "light_off", "sun_night_day_night", "weather_windy_states", "routine_home"), Arrays.asList("blind_rules", "light_rules"), Arrays.asList()));
        this.presets.put("uc3_v3", new Preset(Arrays.asList("blind_devices", "light_devices", "routine_devices", "sun", "weather"), Arrays.asList("blind_states", "light_on", "sun_day_night_day", "weather_clear_states", "routine_home"), Arrays.asList("blind_rules", "light_rules"), Arrays.asList()));
        this.presets.put("uc3_v4", new Preset(Arrays.asList("blind_devices", "light_devices", "routine_devices", "sun", "weather"), Arrays.asList("blind_states", "light_off", "sun_night_day_night", "weather_windy_states", "routine_home"), Arrays.asList("blind_rules", "light_rules"), Arrays.asList()));
        this.presets.put("uc4_v1", new Preset(Arrays.asList("smoke", "routine_devices", "security_devices"), Arrays.asList("routine_workday", "security_states", "smoke_idle"), Arrays.asList("security_rules", "smoke_advanced"), Arrays.asList()));
        this.presets.put("uc4_v2", new Preset(Arrays.asList("smoke", "routine_devices", "security_devices"), Arrays.asList("routine_home", "security_states", "smoke_smoke"), Arrays.asList("security_rules", "smoke_advanced"), Arrays.asList()));
        this.presets.put("uc4_v3", new Preset(Arrays.asList("smoke", "routine_devices", "security_devices"), Arrays.asList("routine_workday", "security_states", "smoke_idle"), Arrays.asList("security_rules", "smoke_advanced"), Arrays.asList()));
        this.presets.put("uc4_v4", new Preset(Arrays.asList("smoke", "routine_devices", "security_devices"), Arrays.asList("routine_weekend_late", "security_states", "smoke_idle"), Arrays.asList("security_rules", "smoke_advanced"), Arrays.asList()));
        this.presets.put("race_condition", new Preset(Arrays.asList("race_condition"), Arrays.asList("race_condition"), Arrays.asList("race_condition"), Arrays.asList()));
        this.presets.put("race_condition_2_min", new Preset(Arrays.asList("race_condition"), Arrays.asList("race_condition_2_min"), Arrays.asList("race_condition"), Arrays.asList()));
        this.presets.put("inconsistencies", new Preset(Arrays.asList("inconsistency"), Arrays.asList("inconsistency"), Arrays.asList("inconsistency"), Arrays.asList()));
        this.presets.put("loops", new Preset(Arrays.asList("loops"), Arrays.asList("loops"), Arrays.asList("loops"), Arrays.asList("")));
        this.presets.put("muc1", new Preset(Arrays.asList("inconsistency"), Arrays.asList("inconsistency_uc1"), Arrays.asList("inconsistency_uc1"), Arrays.asList()));
        this.presets.put("muc2", new Preset(Arrays.asList("inconsistency"), Arrays.asList("inconsistency_uc2"), Arrays.asList("inconsistency_uc2"), Arrays.asList()));
        this.presets.put("muc3", new Preset(Arrays.asList("redundancy"), Arrays.asList("redundancy_uc1"), Arrays.asList("redundancy_uc1"), Arrays.asList()));
        this.presets.put("muc4", new Preset(Arrays.asList("redundancy"), Arrays.asList("redundancy_uc2"), Arrays.asList("redundancy_uc2"), Arrays.asList()));
        this.presets.put("muc5", new Preset(Arrays.asList("loops"), Arrays.asList("loops_uc1"), Arrays.asList("loops_uc1"), Arrays.asList()));
        this.presets.put("cleaning_v1", new Preset(Arrays.asList("cleaning_devices", "routine_devices"), Arrays.asList("cleaning_ongoing", "routine_workday"), Arrays.asList("cleaning_start", "cleaning_stop"), Arrays.asList()));
        this.presets.put("cleaning_v2", new Preset(Arrays.asList("cleaning_devices", "routine_devices"), Arrays.asList("cleaning_idle", "routine_workday"), Arrays.asList("cleaning_start", "cleaning_stop"), Arrays.asList()));
        this.presets.put("cleaning_v3", new Preset(Arrays.asList("cleaning_devices", "routine_devices"), Arrays.asList("cleaning_ongoing", "routine_workday"), Arrays.asList("cleaning_start", "cleaning_stop"), Arrays.asList()));
        this.presets.put("cleaning_v4", new Preset(Arrays.asList("cleaning_devices", "routine_devices"), Arrays.asList("cleaning_idle", "routine_weekend"), Arrays.asList("cleaning_start", "cleaning_stop"), Arrays.asList()));
        this.presets.put("all_conflicts", new Preset(Arrays.asList("light_simple", "smoke", "sun"), Arrays.asList("light_simple", "smoke_idle", "sun_day_night_day"), Arrays.asList("light_simple", "smoke"), Arrays.asList("loops", "inconsistencies", "redundancies")));
        this.presets.put("conflict_temp", new Preset(Arrays.asList("bedroom_temperature", "routine_devices"), Arrays.asList("bedroom_temperature_summer", "routine_sleeping"), Arrays.asList("bedroom_temperature_rules_conflict"), Arrays.asList("udc_bedroom_temperature")));
        this.presets.put("conflict_incidental_inconsistency", new Preset(Arrays.asList("sun", "routine_devices", "light_simple"), Arrays.asList("routine_weekend", "sun_day_night_day", "light_simple"), Arrays.asList("light_simple_conflict"), Arrays.asList("inconsistencies")));
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

    public List<String> getRuleSetOptions() {
        return new ArrayList<>(this.ruleSets.keySet());
    }

    public List<String> getDeviceSetOptions() {
        return new ArrayList<>(this.deviceSets.keySet());
    }
    public List<String> getStateSetOptions() {
        return new ArrayList<>(this.stateSets.keySet());
    }

    public void setDeviceSet(List<String> deviceSets) {
       // System.out.print("Device set: ");
      //  this.printStringList(deviceSets);

        this.activeDeviceSets.clear();

        this.deviceManager.setAllDevicesAvailable(false);
        this.deviceManager.setAllDevicesEnabled(false);
        ArrayList<HassioDevice> devices = new ArrayList<>();

        for(String deviceSet: deviceSets) {
            this.activeDeviceSets.add(deviceSet);

            if(this.deviceSets.get(deviceSet) == null) {
                System.err.println("device set not found: " + deviceSet);
                continue;
            }

            this.deviceSets.get(deviceSet).createDevices(devices);
        }

        for(HassioDevice device: devices) {
            this.deviceManager.addDevice(device);
            device.setAvailable(true);
            device.setEnabled(true);
        }
    }

    public void setRuleSet(List<String> ruleSets) {
      //  System.out.print("Rule sets: ");
     //   this.printStringList(ruleSets);
        
        this.activeRuleSets.clear();

        this.rulesManager.setAllRulesAvailable(false);
        this.rulesManager.setAllRulesEnabled(false);

        for(String rulSet: ruleSets) {
            this.activeRuleSets.add(rulSet);

            if(this.ruleSets.get(rulSet) == null) {
                System.err.println("rule set not found: " + rulSet);
                continue;
            }

            this.ruleSets.get(rulSet).createRules(this.rulesManager, null);
        }
    }

    public void setStateSet(List<String> stateSets) {
        this.deviceManager.clearLogs();
        this.stateScheduler.clearScheduledStates();
        this.activeStateSets.clear();

       // System.out.print("State sets: ");
    //    this.printStringList(stateSets);
        Calendar relativeTime = Calendar.getInstance();
        Date startDate = new Date();

        for(String stateSet: stateSets) {
            this.activeStateSets.add(stateSet);

            relativeTime.setTime(startDate);
            relativeTime.add(Calendar.MINUTE, -240); // Begin 4 uur eerder

            if(this.stateSets.get(stateSet) == null) {
                System.err.println("state set not found: " + stateSet);
                continue;
            }

            this.stateSets.get(stateSet).setInitialStates(this.deviceManager, relativeTime.getTime());

            relativeTime.setTime(startDate);

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

    public void setActivePreset(String activePreset) {
        if(this.presets.containsKey(activePreset)) {
            this.activePreset = activePreset;
            Preset preset = this.presets.get(activePreset);

            setDeviceSet(preset.getNewDeviceSets());
            setRuleSet(preset.getNewRuleSets());
            setStateSet(preset.getNewStateSets());
            setConflictVerifiers(preset.getConflictVerifiers());
        } else {
            System.err.println("unknwon preset: " + activePreset);
            this.activePreset = null;
        }
    }

    private void setConflictVerifiers(List<String> conflictVerifiers) {
        ContextManager.getInstance().getConflictVerificationManager().setActiveVerifiers(conflictVerifiers);
    }

    public List<String> getPresets() {
        return new ArrayList<>(this.presets.keySet());
    }

    public String getActivePreset() {
        return this.activePreset;
    }
}
