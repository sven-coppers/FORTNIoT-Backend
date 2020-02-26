package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.cleaning.HassioCleanerAttributes;
import sven.phd.iot.hassio.climate.HassioCoolerAttributes;
import sven.phd.iot.hassio.sensor.HassioBinarySensorAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.study.StudyStateSet;

import java.util.Calendar;
import java.util.Date;

public class CleaningStates extends StudyStateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(CleaningDevices.ROOMBA_DOWNSTAIRS, "docked", startDate, new HassioCleanerAttributes()));
        DM.logState(new HassioState(CleaningDevices.ROOMBA_UPSTAIRS, "docked", startDate, new HassioCleanerAttributes()));
    }

    @Override
    public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime) {

    }
}
