package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.climate.HassioCoolerAttributes;
import sven.phd.iot.hassio.climate.HassioHeaterAttributes;
import sven.phd.iot.hassio.sensor.HassioSensorAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class BedroomTempStatesSummer extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(BedroomTempDevices.BEDROOM_HEATING, "eco", startDate, new HassioHeaterAttributes()));
        DM.logState(new HassioState(BedroomTempDevices.BEDROOM_TEMPERATURE, "28.3", startDate, new HassioSensorAttributes("temperature", "°C")));
        DM.logState(new HassioState(BedroomTempDevices.BEDROOM_AIRCO, "cooling", startDate, new HassioCoolerAttributes()));

        Calendar relativeTime = Calendar.getInstance();
        relativeTime.setTime(startDate);

        relativeTime.add(Calendar.MINUTE, 30);
        DM.logState(new HassioState(BedroomTempDevices.BEDROOM_TEMPERATURE, "27.8", relativeTime.getTime(), new HassioSensorAttributes("temperature", "°C")));
        relativeTime.add(Calendar.MINUTE, 30);
        DM.logState(new HassioState(BedroomTempDevices.BEDROOM_TEMPERATURE, "27.4", relativeTime.getTime(), new HassioSensorAttributes("temperature", "°C")));
    }

    @Override
    public void scheduleFutureStates(HassioDeviceManager DM, Calendar relativeTime) {

    }
}