package sven.phd.iot.study;

import sven.phd.iot.hassio.Entity;
import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.climate.HassioCoolerAttributes;
import sven.phd.iot.hassio.climate.HassioThermostatAttributes;
import sven.phd.iot.hassio.light.HassioLightAttributes;
import sven.phd.iot.hassio.person.HassioPersonAttributes;
import sven.phd.iot.hassio.routine.HassioRoutineAttributes;
import sven.phd.iot.hassio.sensor.HassioBinarySensorAttributes;
import sven.phd.iot.hassio.sensor.HassioSensorAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.climate.HassioHeaterAttributes;

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

        relativeTime.setTime(startDate);
        relativeTime.add(Calendar.MINUTE, -90); // Begin 90 minuten in het verleden

        if(stateSet.equals("1")) {
            this.setPastIndoorTempStates(relativeTime.getTime());
            this.setPastHabbitStates(relativeTime.getTime());
            this.setPastLightStates(relativeTime.getTime());

            this.setFutureHabbitStates(startDate);
        }
    }

    private void setPastIndoorTempStates(Date startDate) {
        this.hassioDeviceManager.logState(new HassioState(Entity.LIVING_FLOOR_HEATING, "eco", startDate, new HassioHeaterAttributes()));
        this.hassioDeviceManager.logState(new HassioState(Entity.BEDROOM_MASTER_RADIATOR, "eco", startDate, new HassioHeaterAttributes()));
        this.hassioDeviceManager.logState(new HassioState(Entity.BEDROOM_CHILDREN_RADIATOR, "eco", startDate, new HassioHeaterAttributes()));
        this.hassioDeviceManager.logState(new HassioState(Entity.SHOWER_HEATER, "eco", startDate, new HassioHeaterAttributes()));
        this.hassioDeviceManager.logState(new HassioState(Entity.LIVING_TEMPERATURE, "17.0", startDate, new HassioSensorAttributes("temperature", "°C")));
        this.hassioDeviceManager.logState(new HassioState(Entity.BEDROOM_MASTER_TEMPERATURE, "17.0", startDate, new HassioSensorAttributes("temperature", "°C")));
        this.hassioDeviceManager.logState(new HassioState(Entity.BEDROOM_CHILDREN_TEMPERATURE, "17.0", startDate, new HassioSensorAttributes("temperature", "°C")));
        this.hassioDeviceManager.logState(new HassioState(Entity.SHOWER_TEMPERATURE, "17.0", startDate, new HassioSensorAttributes("temperature", "°C")));
        this.hassioDeviceManager.logState(new HassioState(Entity.LIVING_THERMOSTAT, "17.0", startDate, new HassioThermostatAttributes()));
        this.hassioDeviceManager.logState(new HassioState(Entity.BEDROOM_MASTER_THERMOSTAT, "17.0", startDate, new HassioThermostatAttributes()));
        this.hassioDeviceManager.logState(new HassioState(Entity.BEDROOM_CHILDREN_THERMOSTAT, "17.0", startDate, new HassioThermostatAttributes()));
        this.hassioDeviceManager.logState(new HassioState(Entity.SHOWER_THERMOSTAT, "17.0", startDate, new HassioThermostatAttributes()));
        this.hassioDeviceManager.logState(new HassioState(Entity.LIVING_AIRCO, "off", startDate, new HassioCoolerAttributes()));
        this.hassioDeviceManager.logState(new HassioState(Entity.BEDROOM_MASTER_AIRCO, "off", startDate, new HassioCoolerAttributes()));
        this.hassioDeviceManager.logState(new HassioState(Entity.BEDROOM_CHILDREN_AIRCO, "off", startDate, new HassioCoolerAttributes()));
        this.hassioDeviceManager.logState(new HassioState(Entity.SHOWER_VENTILATION, "off", startDate, new HassioCoolerAttributes()));
    }

    private void setPastLightStates(Date startDate) {
        this.hassioDeviceManager.logState(new HassioState(Entity.LIVING_STANDING_LAMP, "off", startDate, new HassioLightAttributes()));
        this.hassioDeviceManager.logState(new HassioState(Entity.LIVING_SPOTS, "off", startDate, new HassioLightAttributes()));
        this.hassioDeviceManager.logState(new HassioState(Entity.KITCHEN_SPOTS, "off", startDate, new HassioLightAttributes()));
    }

    private void setPastHabbitStates(Date startDate) {
        this.hassioDeviceManager.logState(new HassioState(Entity.PEOPLE_DAD, "Away", startDate, new HassioPersonAttributes()));
        this.hassioDeviceManager.logState(new HassioState(Entity.PEOPLE_MOM, "Away", startDate, new HassioPersonAttributes()));
        this.hassioDeviceManager.logState(new HassioState(Entity.ROUTINE, "working", startDate, new HassioRoutineAttributes()));
        this.hassioDeviceManager.logState(new HassioState(Entity.BEDROOM_CHILDREN_MOTION, "clear", startDate, new HassioBinarySensorAttributes()));
    }

    private void setFutureHabbitStates(Date startDate) {
        relativeTime.setTime(startDate);

        relativeTime.add(Calendar.MINUTE, 30);
        this.stateScheduler.scheduleState(new HassioState(Entity.PEOPLE_DAD, "home", relativeTime.getTime(), new HassioPersonAttributes()));
        relativeTime.add(Calendar.MINUTE, 20);
        this.stateScheduler.scheduleState(new HassioState(Entity.PEOPLE_MOM, "home", relativeTime.getTime(), new HassioPersonAttributes()));
        relativeTime.add(Calendar.MINUTE, 50);
        this.stateScheduler.scheduleState(new HassioState(Entity.ROUTINE, "evening", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.MINUTE, 120);
        this.stateScheduler.scheduleState(new HassioState(Entity.ROUTINE, "sleeping", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.MINUTE, 7 * 60);
        this.stateScheduler.scheduleState(new HassioState(Entity.ROUTINE, "morning", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.MINUTE, 45);
        this.stateScheduler.scheduleState(new HassioState(Entity.PEOPLE_DAD, "away", relativeTime.getTime(), new HassioPersonAttributes()));
        relativeTime.add(Calendar.MINUTE, 10);
        this.stateScheduler.scheduleState(new HassioState(Entity.PEOPLE_MOM, "away", relativeTime.getTime(), new HassioPersonAttributes()));
    }
}
