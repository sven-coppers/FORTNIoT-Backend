package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.person.HassioPersonAttributes;
import sven.phd.iot.hassio.routine.HassioRoutineAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class RoutineWorkingStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(RoutineDevices.PEOPLE_DAD, "Away", startDate, new HassioPersonAttributes()));
        DM.logState(new HassioState(RoutineDevices.PEOPLE_MOM, "Away", startDate, new HassioPersonAttributes()));
        DM.logState(new HassioState(RoutineDevices.ROUTINE, "working", startDate, new HassioRoutineAttributes()));
    }

    @Override
    public void scheduleFutureStates(HassioDeviceManager DM, Calendar relativeTime) {
        relativeTime.add(Calendar.MINUTE, 50);
        DM.scheduleState(new HassioState(RoutineDevices.PEOPLE_DAD, "home", relativeTime.getTime(), new HassioPersonAttributes()));
        relativeTime.add(Calendar.MINUTE, 20);
        DM.scheduleState(new HassioState(RoutineDevices.PEOPLE_MOM, "home", relativeTime.getTime(), new HassioPersonAttributes()));
        relativeTime.add(Calendar.MINUTE, 40);
        DM.scheduleState(new HassioState(RoutineDevices.ROUTINE, "evening", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.MINUTE, 240);
        DM.scheduleState(new HassioState(RoutineDevices.ROUTINE, "sleeping", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.MINUTE, 7 * 60);
        DM.scheduleState(new HassioState(RoutineDevices.ROUTINE, "morning", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.MINUTE, 90);
        DM.scheduleState(new HassioState(RoutineDevices.PEOPLE_DAD, "away", relativeTime.getTime(), new HassioPersonAttributes()));
        relativeTime.add(Calendar.MINUTE, 20);
        DM.scheduleState(new HassioState(RoutineDevices.PEOPLE_MOM, "away", relativeTime.getTime(), new HassioPersonAttributes()));
    }
}
