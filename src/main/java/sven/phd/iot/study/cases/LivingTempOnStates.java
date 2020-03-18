package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.climate.HassioThermostatAttributes;
import sven.phd.iot.hassio.sensor.HassioSensorAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.study.StudyStateSet;

import java.util.Calendar;
import java.util.Date;

public class LivingTempOnStates extends StudyStateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(LivingTempDevices.LIVING_FLOOR_HEATING, "eco", startDate, null));
        DM.logState(new HassioState(LivingTempDevices.LIVING_TEMPERATURE, "21.0", startDate, new HassioSensorAttributes("temperature", "°C")));
        DM.logState(new HassioState(LivingTempDevices.LIVING_THERMOSTAT, "21.0", startDate, new HassioThermostatAttributes()));
        DM.logState(new HassioState(LivingTempDevices.LIVING_AIRCO, "off", startDate, null));
    }

    @Override
    public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime) {

    }

}
