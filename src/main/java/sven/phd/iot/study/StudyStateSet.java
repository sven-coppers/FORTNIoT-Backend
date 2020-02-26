package sven.phd.iot.study;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;

import java.util.Calendar;
import java.util.Date;

abstract public class StudyStateSet {
    abstract public void setInitialStates(HassioDeviceManager DM, Date startDate);
    abstract public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime);
}
