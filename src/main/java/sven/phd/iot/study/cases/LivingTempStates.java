package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.climate.HassioCoolerAttributes;
import sven.phd.iot.hassio.climate.HassioHeaterAttributes;
import sven.phd.iot.hassio.climate.HassioThermostatAttributes;
import sven.phd.iot.hassio.sensor.HassioSensorAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.study.StudyStateSet;

import java.util.Calendar;
import java.util.Date;

public class LivingTempStates extends StudyStateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(LivingTempDevices.LIVING_FLOOR_HEATING, "eco", startDate, new HassioHeaterAttributes()));
        DM.logState(new HassioState(LivingTempDevices.LIVING_TEMPERATURE, "17.0", startDate, new HassioSensorAttributes("temperature", "°C")));
        DM.logState(new HassioState(LivingTempDevices.LIVING_THERMOSTAT, "17.0", startDate, new HassioThermostatAttributes()));
        DM.logState(new HassioState(LivingTempDevices.LIVING_AIRCO, "off", startDate, new HassioCoolerAttributes()));
    }

    @Override
    public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime) {

    }

}
