package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.climate.HassioThermostatAttributes;
import sven.phd.iot.hassio.sensor.HassioSensorAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class LivingTempStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(LivingTempDevices.LIVING_FLOOR_HEATING, "eco", startDate, null));
        DM.logState(new HassioState(LivingTempDevices.LIVING_TEMPERATURE, "17.0", startDate, new HassioSensorAttributes("temperature", "°C")));
        DM.logState(new HassioState(LivingTempDevices.LIVING_THERMOSTAT, "17.0", startDate, new HassioThermostatAttributes()));
        DM.logState(new HassioState(LivingTempDevices.LIVING_AIRCO, "off", startDate, null));
    }

    @Override
    public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime) {

    }

}
