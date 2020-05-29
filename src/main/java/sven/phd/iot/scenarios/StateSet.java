package sven.phd.iot.scenarios;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;

import java.util.Calendar;
import java.util.Date;

abstract public class StateSet {
    abstract public void setInitialStates(HassioDeviceManager DM, Date startDate);
    abstract public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime);
    protected static long getTime(int hours, int minutes) {
        return getTime(hours, minutes, 0);
    }
    protected static long getTime(int hours, int minutes, int seconds) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);
        cal.set(Calendar.SECOND, seconds);

        return cal.getTime().getTime();
    }
}
