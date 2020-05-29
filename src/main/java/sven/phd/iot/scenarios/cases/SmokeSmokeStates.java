package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class SmokeSmokeStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(SmokeDevices.SENSOR_LIVING_SMOKE, "idle", startDate, null));
    }

    @Override
    public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime) {
        relativeTime.add(Calendar.HOUR, 2);
        SS.scheduleState(new HassioState(SmokeDevices.SENSOR_LIVING_SMOKE, "smoke detected", relativeTime.getTime(), null));
        relativeTime.add(Calendar.HOUR, 2);
        SS.scheduleState(new HassioState(SmokeDevices.SENSOR_LIVING_SMOKE, "idle", relativeTime.getTime(), null));
    }
}