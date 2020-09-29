package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.person.HassioPersonAttributes;
import sven.phd.iot.hassio.routine.HassioRoutineAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class WeekDayLateRoutineStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(WeekdayRoutineDevices.PEOPLE_DAD, "Away", startDate, new HassioPersonAttributes()));
        DM.logState(new HassioState(WeekdayRoutineDevices.PEOPLE_MOM, "Away", startDate, new HassioPersonAttributes()));
        DM.logState(new HassioState(WeekdayRoutineDevices.ROUTINE, "working", startDate, new HassioRoutineAttributes()));
    }

    @Override
    public void scheduleFutureStates(HassioDeviceManager DM, Calendar relativeTime) {
        relativeTime.add(Calendar.MINUTE, 30);
        DM.scheduleState(new HassioState(WeekdayRoutineDevices.ROUTINE, "evening", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.MINUTE, 120);
        DM.scheduleState(new HassioState(WeekdayRoutineDevices.PEOPLE_DAD, "home", relativeTime.getTime(), new HassioPersonAttributes()));
        DM.scheduleState(new HassioState(WeekdayRoutineDevices.PEOPLE_MOM, "home", relativeTime.getTime(), new HassioPersonAttributes()));
        relativeTime.add(Calendar.MINUTE, 2);
        DM.scheduleState(new HassioState(WeekdayRoutineDevices.ROUTINE, "sleeping", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.MINUTE, 7 * 60);
        DM.scheduleState(new HassioState(WeekdayRoutineDevices.ROUTINE, "morning", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.MINUTE, 45);
        DM.scheduleState(new HassioState(WeekdayRoutineDevices.PEOPLE_DAD, "away", relativeTime.getTime(), new HassioPersonAttributes()));
        relativeTime.add(Calendar.MINUTE, 10);
        DM.scheduleState(new HassioState(WeekdayRoutineDevices.PEOPLE_MOM, "away", relativeTime.getTime(), new HassioPersonAttributes()));
    }
}
