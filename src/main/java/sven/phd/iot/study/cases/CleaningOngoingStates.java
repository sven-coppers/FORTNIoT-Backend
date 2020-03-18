package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.cleaning.HassioCleanerAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.study.StudyStateSet;

import java.util.Calendar;
import java.util.Date;

public class CleaningOngoingStates extends StudyStateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(CleaningDevices.ROOMBA_DOWNSTAIRS, "docked", startDate, new HassioCleanerAttributes()));
        DM.logState(new HassioState(CleaningDevices.ROOMBA_DOWNSTAIRS_BATTERY, "100", startDate, null));

        DM.logState(new HassioState(CleaningDevices.ROOMBA_UPSTAIRS, "docked", startDate, new HassioCleanerAttributes()));
        DM.logState(new HassioState(CleaningDevices.ROOMBA_UPSTAIRS_BATTERY, "100", startDate, null));

        Calendar relativeTime = Calendar.getInstance();
        relativeTime.setTime(startDate);
        relativeTime.add(Calendar.MINUTE, 200);

        DM.logState(new HassioState(CleaningDevices.ROOMBA_UPSTAIRS, "cleaning", relativeTime.getTime(), new HassioCleanerAttributes()));
        DM.logState(new HassioState(CleaningDevices.ROOMBA_UPSTAIRS_BATTERY, "100", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 30);
        DM.logState(new HassioState(CleaningDevices.ROOMBA_UPSTAIRS_BATTERY, "85", relativeTime.getTime(), null));
    }

    @Override
    public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime) {

    }
}
