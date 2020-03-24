package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.cleaning.HassioCleanerAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class CleaningStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(CleaningDevices.ROOMBA_DOWNSTAIRS, "docked", startDate, new HassioCleanerAttributes()));
        DM.logState(new HassioState(CleaningDevices.ROOMBA_DOWNSTAIRS_BATTERY, "100", startDate, null));

        DM.logState(new HassioState(CleaningDevices.ROOMBA_UPSTAIRS, "docked", startDate, new HassioCleanerAttributes()));
        DM.logState(new HassioState(CleaningDevices.ROOMBA_UPSTAIRS_BATTERY, "100", startDate, null));
    }

    @Override
    public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime) {

    }
}
