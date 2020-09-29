package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.person.HassioPersonAttributes;
import sven.phd.iot.hassio.routine.HassioRoutineAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class RoutineWeekendStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(RoutineDevices.PEOPLE_DAD, "home", startDate, new HassioPersonAttributes()));
        DM.logState(new HassioState(RoutineDevices.PEOPLE_MOM, "home", startDate, new HassioPersonAttributes()));
        DM.logState(new HassioState(RoutineDevices.ROUTINE, "morning", startDate, new HassioRoutineAttributes()));
    }

    @Override
    public void scheduleFutureStates(HassioDeviceManager DM, Calendar relativeTime) {
        relativeTime.add(Calendar.HOUR, 6);
        DM.scheduleState(new HassioState(RoutineDevices.PEOPLE_MOM, "away", relativeTime.getTime(), new HassioPersonAttributes()));
        relativeTime.add(Calendar.MINUTE, 5);
        DM.scheduleState(new HassioState(RoutineDevices.PEOPLE_DAD, "away", relativeTime.getTime(), new HassioPersonAttributes()));
        DM.scheduleState(new HassioState(RoutineDevices.ROUTINE, "leisure", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.HOUR, 6);
        DM.scheduleState(new HassioState(RoutineDevices.ROUTINE, "evening", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.HOUR, 2);
        DM.scheduleState(new HassioState(RoutineDevices.PEOPLE_DAD, "home", relativeTime.getTime(), new HassioPersonAttributes()));
        DM.scheduleState(new HassioState(RoutineDevices.PEOPLE_MOM, "home", relativeTime.getTime(), new HassioPersonAttributes()));
        relativeTime.add(Calendar.HOUR, 1);
        DM.scheduleState(new HassioState(RoutineDevices.ROUTINE, "sleeping", relativeTime.getTime(), new HassioRoutineAttributes()));
    }
}
