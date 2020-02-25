package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.sun.HassioSunAttributes;
import sven.phd.iot.study.StudyStateSet;

import java.util.Calendar;
import java.util.Date;

public class VirtualSunStates extends StudyStateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        Calendar relativeTime = Calendar.getInstance();
        relativeTime.setTime(startDate);

        HassioSunAttributes sunAttributes = new HassioSunAttributes(30.0f, true);

        relativeTime.add(Calendar.HOUR, 1);
        sunAttributes.nextNoon = relativeTime.getTime();
        relativeTime.add(Calendar.HOUR, 4);
        sunAttributes.nextSetting = relativeTime.getTime();
        relativeTime.add(Calendar.HOUR, 4);
        sunAttributes.nextMidnight = relativeTime.getTime();
        relativeTime.add(Calendar.HOUR, 4);
        sunAttributes.nextRising = relativeTime.getTime();

        DM.logState(new HassioState("sun.sun", "above_horizon", startDate, sunAttributes));
    }

    @Override
    public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime) {

    }
}
