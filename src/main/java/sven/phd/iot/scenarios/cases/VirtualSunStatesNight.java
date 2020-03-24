package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.sun.HassioSunAttributes;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class VirtualSunStatesNight extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        Calendar relativeTime = Calendar.getInstance();
        relativeTime.setTime(startDate);

        HassioSunAttributes sunAttributes = new HassioSunAttributes(30.0f, true);

        relativeTime.add(Calendar.HOUR, 2);
        sunAttributes.nextMidnight = relativeTime.getTime();
        relativeTime.add(Calendar.HOUR, 6);
        sunAttributes.nextRising = relativeTime.getTime();
        relativeTime.add(Calendar.HOUR, 5);
        sunAttributes.nextNoon = relativeTime.getTime();
        relativeTime.add(Calendar.HOUR, 5);
        sunAttributes.nextSetting = relativeTime.getTime();

        DM.logState(new HassioState("sun.sun", "below_horizon", startDate, sunAttributes));
    }

    @Override
    public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime) {

    }
}
