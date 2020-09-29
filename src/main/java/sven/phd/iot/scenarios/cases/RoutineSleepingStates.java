package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.person.HassioPersonAttributes;
import sven.phd.iot.hassio.routine.HassioRoutineAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class RoutineSleepingStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(RoutineDevices.PEOPLE_DAD, "home", startDate, new HassioPersonAttributes()));
        DM.logState(new HassioState(RoutineDevices.PEOPLE_MOM, "home", startDate, new HassioPersonAttributes()));
        DM.logState(new HassioState(RoutineDevices.ROUTINE, "sleeping", startDate, new HassioRoutineAttributes()));
    }

    @Override
    public void scheduleFutureStates(HassioDeviceManager DM, Calendar relativeTime) {
        relativeTime.add(Calendar.MINUTE, 230);
        DM.scheduleState(new HassioState(RoutineDevices.ROUTINE, "morning", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.MINUTE, 120);
        DM.scheduleState(new HassioState(RoutineDevices.ROUTINE, "working from home", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.MINUTE, 8 * 60);
        DM.scheduleState(new HassioState(RoutineDevices.ROUTINE, "evening", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.MINUTE, 270);
        DM.scheduleState(new HassioState(RoutineDevices.ROUTINE, "sleeping", relativeTime.getTime(), new HassioRoutineAttributes()));
    }
}