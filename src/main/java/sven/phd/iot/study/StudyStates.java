package sven.phd.iot.study;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.person.HassioPersonAttributes;
import sven.phd.iot.hassio.sensor.HassioSensorAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.thermostat.HassioThermostatAttributes;

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
        Calendar relativeTime = Calendar.getInstance();
        relativeTime.setTime(new Date());
        relativeTime.add(Calendar.MINUTE, -90); // Begin 90 minuten in het verleden

        // Past States
        this.hassioDeviceManager.getDevice("person.dad").logState(new HassioState("person.dad", "Away", relativeTime.getTime(), new HassioPersonAttributes()));
        this.hassioDeviceManager.getDevice("heater.heater").logState(new HassioState("heater.heater", "off", relativeTime.getTime(), new HassioThermostatAttributes(15)));
        this.hassioDeviceManager.getDevice("sensor.indoor_temperature_measurement").logState(new HassioState("sensor.indoor_temperature_measurement", "15.5", relativeTime.getTime(), new HassioSensorAttributes("temperature", "°C")));

        relativeTime.add(Calendar.MINUTE, 120);

        // Future State
        // Always log the first state of every device
        //
        //    this.hassioDeviceMap.get("airco.airco").logState(new HassioState("airco.airco", "off", relativeTime.getTime(), new HassioThermostatAttributes(21)));
        //    this.hassioDeviceMap.get("light.standing_lamp").logState(new HassioState("light.standing_lamp", "off", relativeTime.getTime(), new HassioLightAttributes()));
        //    this.hassioDeviceMap.get("light.kitchen_spots").logState(new HassioState("light.kitchen_spots", "off", relativeTime.getTime(), new HassioLightAttributes()));
        //    this.hassioDeviceMap.get("light.living_spots").logState(new HassioState("light.living_spots", "off", relativeTime.getTime(), new HassioLightAttributes()));
        //    this.hassioDeviceMap.get("sensor.outdoor_temperature_measurement").logState(new HassioState("sensor.outdoor_temperature_measurement", "5", relativeTime.getTime(), new HassioSensorAttributes("temperature", "°C")));
        //    this.hassioDeviceMap.get("person.dad").logState(new HassioState("person.dad", "Away", relativeTime.getTime(), new HassioPersonAttributes()));
        //    this.hassioDeviceMap.get("person.mom").logState(new HassioState("person.mom", "Away", relativeTime.getTime(), new HassioPersonAttributes()));
        //   this.hassioDeviceMap.get("sensor.people_home_counter").logState(new HassioState("sensor.people_home_counter", "0", relativeTime.getTime(), new HassioSensorAttributes("people", "people")));

       // this.stateScheduler.scheduleState(new HassioState("sensor.indoor_temperature_measurement", "15.3", relativeTime.getTime(), new HassioSensorAttributes("temperature", "°C"))); relativeTime.add(Calendar.MINUTE, 20);
       // this.stateScheduler.scheduleState(new HassioState("sensor.indoor_temperature_measurement", "19", relativeTime.getTime(), new HassioSensorAttributes("temperature", "°C"))); relativeTime.add(Calendar.MINUTE, 20);
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
