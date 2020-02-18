package sven.phd.iot.study;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.person.HassioPersonAttributes;
import sven.phd.iot.hassio.sensor.HassioSensorAttributes;
import sven.phd.iot.hassio.states.HassioState;

import java.util.Calendar;
import java.util.Date;

public class StudyStates {
    private HassioDeviceManager hassioDeviceManager;
    private HassioStateScheduler stateScheduler;
    private String stateSet;
    private Calendar relativeTime;

    public StudyStates(HassioDeviceManager hassioDeviceManager) {
        this.hassioDeviceManager = hassioDeviceManager;
        this.stateScheduler = hassioDeviceManager.getStateScheduler();
        this.relativeTime = Calendar.getInstance();
        this.setStateSet("all");
    }

    public String getStateSet() { return stateSet; }
    public void setStateSet(String stateSet) {
        System.out.println("State set: " + stateSet);
        this.stateSet = stateSet;

        this.stateScheduler.clearScheduledStates();
        Date startDate = new Date();

        if(stateSet.equals("1")) {
            this.setIndoorTempStates(startDate);
            this.setDaddyStates(startDate);
        }
    }

    private void setIndoorTempStates(Date startDate) {
        relativeTime.setTime(startDate);
        relativeTime.add(Calendar.MINUTE, -15);

        this.stateScheduler.scheduleState(new HassioState("sensor.indoor_temperature_measurement", "20", relativeTime.getTime(), new HassioSensorAttributes("temperature", "°C"))); relativeTime.add(Calendar.MINUTE, 20);
        this.stateScheduler.scheduleState(new HassioState("sensor.indoor_temperature_measurement", "19", relativeTime.getTime(), new HassioSensorAttributes("temperature", "°C"))); relativeTime.add(Calendar.MINUTE, 20);
       // this.stateScheduler.scheduleState(new HassioState("sensor.indoor_temperature_measurement", "18", relativeTime.getTime(), new HassioSensorAttributes("temperature", "°C"))); relativeTime.add(Calendar.MINUTE, 20);
       // this.stateScheduler.scheduleState(new HassioState("sensor.indoor_temperature_measurement", "17", relativeTime.getTime(), new HassioSensorAttributes("temperature", "°C"))); relativeTime.add(Calendar.MINUTE, 20);
       // this.stateScheduler.scheduleState(new HassioState("sensor.indoor_temperature_measurement", "16", relativeTime.getTime(), new HassioSensorAttributes("temperature", "°C"))); relativeTime.add(Calendar.MINUTE, 20);
       // this.stateScheduler.scheduleState(new HassioState("sensor.indoor_temperature_measurement", "15", relativeTime.getTime(), new HassioSensorAttributes("temperature", "°C"))); relativeTime.add(Calendar.MINUTE, 10);
       // this.stateScheduler.scheduleState(new HassioState("sensor.indoor_temperature_measurement", "16", relativeTime.getTime(), new HassioSensorAttributes("temperature", "°C"))); relativeTime.add(Calendar.MINUTE, 20);
       // this.stateScheduler.scheduleState(new HassioState("sensor.indoor_temperature_measurement", "15", relativeTime.getTime(), new HassioSensorAttributes("temperature", "°C")));
    }

    private void setDaddyStates(Date startDate) {
        relativeTime.setTime(startDate);
        relativeTime.add(Calendar.MINUTE, 85);

        this.stateScheduler.scheduleState(new HassioState("person.dad", "home", relativeTime.getTime(), new HassioPersonAttributes()));
        relativeTime.add(Calendar.MINUTE, 20);
        this.stateScheduler.scheduleState(new HassioState("person.mom", "home", relativeTime.getTime(), new HassioPersonAttributes()));
    }


    // Other states can be used to predict
  /*      this.stateScheduler.scheduleState(new HassioState("light.standing_lamp", "on", relativeTime.getTime(), new HassioLightAttributes()));
        relativeTime.add(Calendar.MINUTE, 5);
        this.stateScheduler.scheduleState(new HassioState("light.standing_lamp", "off", relativeTime.getTime(), new HassioLightAttributes()));
        relativeTime.add(Calendar.MINUTE, 35);
        this.stateScheduler.scheduleState(new HassioState("light.standing_lamp", "on", relativeTime.getTime(), new HassioLightAttributes()));
        relativeTime.add(Calendar.MINUTE, 60);

        relativeTime.setTime(new Date());
        relativeTime.add(Calendar.MINUTE, -20); // Begin 30 minuten in het verleden
        this.stateScheduler.scheduleState(new HassioState("light.kitchen_spots", "on", relativeTime.getTime(), new HassioLightAttributes()));
        relativeTime.add(Calendar.MINUTE, 5);
        this.stateScheduler.scheduleState(new HassioState("light.kitchen_spots", "off", relativeTime.getTime(), new HassioLightAttributes()));

        relativeTime.setTime(new Date());
        this.stateScheduler.scheduleState(new HassioState("light.living_spots", "on", relativeTime.getTime(), new HassioLightAttributes()));
        relativeTime.add(Calendar.MINUTE, 5);
        relativeTime.add(Calendar.MINUTE, 35);

        this.stateScheduler.scheduleState(new HassioState("heater.heater", "heating", relativeTime.getTime(), new HassioThermostatAttributes(21)));
        relativeTime.add(Calendar.MINUTE, 35);
        this.stateScheduler.scheduleState(new HassioState("heater.heater", "eco", relativeTime.getTime(), new HassioThermostatAttributes(21))); */
}
