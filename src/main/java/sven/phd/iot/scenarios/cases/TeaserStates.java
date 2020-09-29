package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.light.HassioLightAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.sun.HassioSunAttributes;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class TeaserStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        Calendar relativeTime = Calendar.getInstance();
        relativeTime.setTime(startDate);

        HassioSunAttributes sunAttributes = new HassioSunAttributes(30.0f, true);

        relativeTime.add(Calendar.HOUR, 5);
        sunAttributes.nextSetting = relativeTime.getTime();
        relativeTime.add(Calendar.HOUR, 6);
        sunAttributes.nextMidnight = relativeTime.getTime();
        relativeTime.add(Calendar.HOUR, 6);
        sunAttributes.nextRising = relativeTime.getTime();
        relativeTime.add(Calendar.HOUR, 4);
        sunAttributes.nextNoon = relativeTime.getTime();

        DM.logState(new HassioState("sun.sun", "above_horizon", startDate, sunAttributes));
        DM.logState(new HassioState(LightSimpleDevices.LIVING_SPOTS, "on", startDate, new HassioLightAttributes()));
        DM.logState(new HassioState(SmokeDevices.SENSOR_LIVING_SMOKE, "idle", startDate, null));
    }

    @Override
    public void scheduleFutureStates(HassioDeviceManager DM, Calendar relativeTime) {
        relativeTime.add(Calendar.HOUR, 8);
        DM.scheduleState(new HassioState(SmokeDevices.SENSOR_LIVING_SMOKE, "smoke detected", relativeTime.getTime(), null));
        relativeTime.add(Calendar.HOUR, 6);
        DM.scheduleState(new HassioState(SmokeDevices.SENSOR_LIVING_SMOKE, "idle", relativeTime.getTime(), null));
    }
}
