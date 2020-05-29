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
    private String preset;

    private List<String> activeDeviceSets;
    private List<String> activeRuleSets;
    private List<String> activeStateSets;
    private List<String> presets;

    public ScenarioManager(ContextManager contextManager) {
        System.out.println("StudyManager - Initiating...");
        this.contextManager = contextManager;
        this.deviceManager = contextManager.getHassioDeviceManager();
        this.stateScheduler = deviceManager.getStateScheduler();
        this.rulesManager = contextManager.getRulesManager();

        this.presets = new ArrayList<>();
        this.presets.add("tr_v1");
        this.presets.add("tr_v2");
        this.presets.add("tr_v3");
        this.presets.add("tr_v4");
        this.presets.add("uc1_v1");
        this.presets.add("uc1_v2");
        this.presets.add("uc1_v3");
        this.presets.add("uc1_v4");
        this.presets.add("uc2_v1");
        this.presets.add("uc2_v2");
        this.presets.add("uc2_v3");
        this.presets.add("uc2_v4");
        this.presets.add("uc3_v1");
        this.presets.add("uc3_v2");
        this.presets.add("uc3_v3");
        this.presets.add("uc3_v4");
        this.presets.add("uc4_v1");
        this.presets.add("uc4_v2");
        this.presets.add("uc4_v3");
        this.presets.add("uc4_v4");

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
        this.stateSets.put("routine_weekend_late", new RoutineWeekendLateStates());
        this.stateSets.put("routine_home", new RoutineHomeStates());
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


        // MATHIAS SETUP
        this.activeRuleSets.add("light_rules");
        this.activeDeviceSets.add("light_devices");
        this.activeDeviceSets.add("sun");
        this.activeStateSets.add("light_off");
        this.activeStateSets.add("light_on");
        this.activeStateSets.add("light_simple");
        this.activeStateSets.add("sun_day_night_day");
        this.activeStateSets.add("sun_night_day_night");

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
        System.out.print("Rule sets: ");
        this.printStringList(ruleSets);
        
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

        System.out.print("State sets: ");
        this.printStringList(stateSets);
        Calendar relativeTime = Calendar.getInstance();
        Date startDate = new Date();

        for(String stateSet: stateSets) {
            this.activeStateSets.add(stateSet);

            relativeTime.setTime(startDate);
            relativeTime.add(Calendar.MINUTE, -240); // Begin 4 uur

            if(this.stateSets.get(stateSet) == null) {
                System.err.println("state set not found: " + stateSet);
                continue;
            }

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

    public void setPreset(String preset) {
        this.preset = preset;
        List<String> newDeviceSets = new ArrayList();
        List<String> newRuleSets = new ArrayList();
        List<String> newStateSets = new ArrayList();

        if(preset.contains("tr")) {
            newDeviceSets.add("light_simple");
            newDeviceSets.add("smoke");
            newDeviceSets.add("sun");

            newRuleSets.add("light_simple");
            newRuleSets.add("smoke");
            if (preset.contains("v1")) {
                newStateSets.add("light_simple");
                newStateSets.add("smoke_idle");
                newStateSets.add("sun_day_night_day");
            } else if (preset.contains("v2")) {
                newStateSets.add("light_simple");
                newStateSets.add("smoke_smoke");
                newStateSets.add("sun_night_day_night");
            } else if (preset.contains("v3")) {
                newStateSets.add("light_simple");
                newStateSets.add("smoke_idle");
                newStateSets.add("sun_night_day_night");
            } else if (preset.contains("v4")) {
                newStateSets.add("light_simple");
                newStateSets.add("smoke_smoke");
                newStateSets.add("sun_day_night_day");
            }
        }  else if(preset.contains("uc1")) {
            newRuleSets.add("tv_rules");
            newDeviceSets.add("light_devices");
            newDeviceSets.add("routine_devices");
            newDeviceSets.add("tv_devices");

            if(preset.contains("v1")) {
                newStateSets.add("light_off");
                newStateSets.add("routine_workday");
                newStateSets.add("tv_news");
            } else if(preset.contains("v2")) {
                newStateSets.add("light_off");
                newStateSets.add("routine_workday");
                newStateSets.add("tv_sports");
            } else if(preset.contains("v3")) {
                newStateSets.add("light_on");
                newStateSets.add("routine_home");
                newStateSets.add("tv_movies");
            } else if(preset.contains("v4")) {
                newStateSets.add("light_on");
                newStateSets.add("routine_weekend");
                newStateSets.add("tv_sports_late");
            }
        }  else if(preset.contains("uc2")) {
            newRuleSets.add("living_temperature");
            newDeviceSets.add("living_temperature");
            newDeviceSets.add("routine_devices");

            if(preset.contains("v1")) {
                newStateSets.add("living_temperature_off");
                newStateSets.add("routine_workday");
            } else if(preset.contains("v2")) {
                newStateSets.add("living_temperature_off");
                newStateSets.add("routine_workday");
            } else if(preset.contains("v3")) {
                newStateSets.add("living_temperature_on");
                newStateSets.add("routine_weekend");
            } else if(preset.contains("v4")) {
                newStateSets.add("living_temperature_on");
                newStateSets.add("routine_weekend");
            }
        }  else if(preset.contains("uc3")) {
            newRuleSets.add("blind_rules");
            newRuleSets.add("light_rules");
            newDeviceSets.add("blind_devices");
            newDeviceSets.add("light_devices");
            newDeviceSets.add("routine_devices");
            newDeviceSets.add("sun");
            newDeviceSets.add("weather");

            if(preset.contains("v1")) {
                newStateSets.add("blind_states");
                newStateSets.add("light_off");
                newStateSets.add("sun_day_night_day");
                newStateSets.add("weather_clear_states");
                newStateSets.add("routine_home");
            } else if(preset.contains("v2")) {
                newStateSets.add("blind_states");
                newStateSets.add("light_off");
                newStateSets.add("sun_night_day_night");
                newStateSets.add("weather_windy_states");
                newStateSets.add("routine_home");
            } else if(preset.contains("v3")) {
                newStateSets.add("blind_states");
                newStateSets.add("light_on");
                newStateSets.add("sun_day_night_day");
                newStateSets.add("weather_clear_states");
                newStateSets.add("routine_home");
            } else if(preset.contains("v4")) {
                newStateSets.add("blind_states");
                newStateSets.add("light_off");
                newStateSets.add("sun_night_day_night");
                newStateSets.add("weather_windy_states");
                newStateSets.add("routine_home");
            }
        }  else if(preset.contains("uc4")) {
            newRuleSets.add("security_rules");
            newRuleSets.add("smoke_advanced");
            newDeviceSets.add("routine_devices");
            newDeviceSets.add("security_devices");
            newDeviceSets.add("smoke");

            if(preset.contains("v1")) {
                newStateSets.add("routine_workday");
                newStateSets.add("security_states");
                newStateSets.add("smoke_idle");
            } else if(preset.contains("v2")) {
                newStateSets.add("routine_home");
                newStateSets.add("security_states");
                newStateSets.add("smoke_smoke");
            } else if(preset.contains("v3")) {
                newStateSets.add("routine_workday");
                newStateSets.add("security_states");
                newStateSets.add("smoke_idle");
            } else if(preset.contains("v4")) {
                newStateSets.add("routine_weekend_late");
                newStateSets.add("security_states");
                newStateSets.add("smoke_idle");
            }
        } else if(preset.contains("cleaning")) {
            newRuleSets.add("cleaning_start");
            newRuleSets.add("cleaning_stop");
            newDeviceSets.add("cleaning_devices");
            newDeviceSets.add("routine_devices");

            if(preset.contains("v1")) {
                newStateSets.add("cleaning_ongoing");
                newStateSets.add("routine_workday");
            } else if(preset.contains("v2")) {
                newStateSets.add("cleaning_idle");
                newStateSets.add("routine_workday");
            } else if(preset.contains("v3")) {
                newStateSets.add("cleaning_ongoing");
                newStateSets.add("routine_workday");
            } else if(preset.contains("v4")) {
                newStateSets.add("cleaning_idle");
                newStateSets.add("routine_weekend");
            }
        } else {
            System.err.println("unknwon preset: " + preset);
            this.preset = null;
        }

        setDeviceSet(newDeviceSets);
        setRuleSet(newRuleSets);
        setStateSet(newStateSets);
    }

    public List<String> getPresets() {
        return this.presets;
    }

    public String getPreset() {
        return this.preset;
    }
}
