package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.climate.HassioThermostatAttributes;
import sven.phd.iot.hassio.sensor.HassioBinarySensorAttributes;
import sven.phd.iot.hassio.sensor.HassioSensorAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class ChildrenTempStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(ChildrenTempDevices.BEDROOM_CHILDREN_RADIATOR, "eco", startDate, null));
        DM.logState(new HassioState(ChildrenTempDevices.BEDROOM_CHILDREN_TEMPERATURE, "17.0", startDate, new HassioSensorAttributes("temperature", "°C")));
        DM.logState(new HassioState(ChildrenTempDevices.BEDROOM_CHILDREN_THERMOSTAT, "17.0", startDate, new HassioThermostatAttributes()));
        DM.logState(new HassioState(ChildrenTempDevices.BEDROOM_CHILDREN_AIRCO, "off", startDate, null));
        DM.logState(new HassioState(ChildrenTempDevices.BEDROOM_CHILDREN_MOTION, "off", startDate, new HassioBinarySensorAttributes()));
    }

    @Override
    public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime) {

    }
}
