package sven.phd.iot.scenarios;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;

import java.util.Calendar;
import java.util.Date;

abstract public class StateSet {
    abstract public void setInitialStates(HassioDeviceManager DM, Date startDate);
    abstract public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime);
}
