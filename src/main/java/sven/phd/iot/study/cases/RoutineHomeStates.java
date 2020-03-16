package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.person.HassioPersonAttributes;
import sven.phd.iot.hassio.routine.HassioRoutineAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.study.StudyStateSet;

import java.util.Calendar;
import java.util.Date;

public class RoutineHomeStates extends StudyStateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(RoutineDevices.PEOPLE_DAD, "home", startDate, new HassioPersonAttributes()));
        DM.logState(new HassioState(RoutineDevices.PEOPLE_MOM, "home", startDate, new HassioPersonAttributes()));
        DM.logState(new HassioState(RoutineDevices.ROUTINE, "working from home", startDate, new HassioRoutineAttributes()));
    }

    @Override
    public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime) {
        relativeTime.add(Calendar.MINUTE, 230);
        SS.scheduleState(new HassioState(RoutineDevices.ROUTINE, "evening", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.MINUTE, 240);
        SS.scheduleState(new HassioState(RoutineDevices.ROUTINE, "sleeping", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.MINUTE, 7 * 60);
        SS.scheduleState(new HassioState(RoutineDevices.ROUTINE, "morning", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.MINUTE, 90);
        SS.scheduleState(new HassioState(RoutineDevices.PEOPLE_DAD, "away", relativeTime.getTime(), new HassioPersonAttributes()));
        relativeTime.add(Calendar.MINUTE, 20);
        SS.scheduleState(new HassioState(RoutineDevices.PEOPLE_MOM, "away", relativeTime.getTime(), new HassioPersonAttributes()));
    }
}
