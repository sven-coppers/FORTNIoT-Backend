package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.climate.HassioThermostatAttributes;
import sven.phd.iot.hassio.sensor.HassioSensorAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class ShowerTempStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(ShowerTempDevices.SHOWER_HEATER, "eco", startDate, null));
        DM.logState(new HassioState(ShowerTempDevices.SHOWER_VENTILATION, "off", startDate, null));
        DM.logState(new HassioState(ShowerTempDevices.SHOWER_TEMPERATURE, "17.0", startDate, new HassioSensorAttributes("temperature", "°C")));
        DM.logState(new HassioState(ShowerTempDevices.SHOWER_THERMOSTAT, "17.0", startDate, new HassioThermostatAttributes()));
    }

    @Override
    public void scheduleFutureStates(HassioDeviceManager DM, Calendar relativeTime) {

    }
}
