package sven.phd.iot.study;

import sven.phd.iot.hassio.HassioDeviceManager;

public class StudyDevices {
    private HassioDeviceManager deviceManager;
    private String deviceSet;

    public StudyDevices(HassioDeviceManager deviceManager) {
        System.out.println("StudyManager - Initiating...");
        this.deviceManager = deviceManager;
        this.setDeviceSet("all");
    }

    public void setDeviceSet(String deviceSet) {
        System.out.println("Device set: " + deviceSet);
        this.deviceSet = deviceSet;

        this.deviceManager.setAllDevicesAvailable(false);

        if(deviceSet.equals("all")) {
            this.deviceManager.setAllDevicesAvailable(true);
        } else if(deviceSet.equals("1")) {
            this.deviceManager.setDeviceAvailable("heater.heater", true);
            this.deviceManager.setDeviceAvailable("sensor.indoor_temperature_measurement", true);
            this.deviceManager.setDeviceAvailable("person.dad", true);
            this.deviceManager.setDeviceAvailable("sensor.people_home_counter", true);
        } else if(deviceSet.equals("4")) {
            this.deviceManager.setDeviceAvailable("sensor.button_1_battery", true);
            this.deviceManager.setDeviceAvailable("sensor.button_2_battery", true);
            this.deviceManager.setDeviceAvailable("sensor.agoralaan_diepenbeek", true);
            this.deviceManager.setDeviceAvailable("sun.sun", true);
        }
    }

    public String getDeviceSet() {
        return deviceSet;
    }
}
