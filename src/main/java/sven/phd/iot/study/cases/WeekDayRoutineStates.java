package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.person.HassioPersonAttributes;
import sven.phd.iot.hassio.routine.HassioRoutineAttributes;
import sven.phd.iot.hassio.sensor.HassioBinarySensorAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.study.StudyStateSet;

import java.util.Calendar;
import java.util.Date;

public class WeekDayRoutineStates extends StudyStateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(WeekdayRoutineDevices.PEOPLE_DAD, "Away", startDate, new HassioPersonAttributes()));
        DM.logState(new HassioState(WeekdayRoutineDevices.PEOPLE_MOM, "Away", startDate, new HassioPersonAttributes()));
        DM.logState(new HassioState(WeekdayRoutineDevices.ROUTINE, "working", startDate, new HassioRoutineAttributes()));
        //DM.logState(new HassioState(WeekdayRoutineDevices.BEDROOM_CHILDREN_MOTION, "clear", startDate, new HassioBinarySensorAttributes())
    }

    @Override
    public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime) {
        relativeTime.add(Calendar.MINUTE, 30);
        SS.scheduleState(new HassioState(WeekdayRoutineDevices.PEOPLE_DAD, "home", relativeTime.getTime(), new HassioPersonAttributes()));
        relativeTime.add(Calendar.MINUTE, 20);
        SS.scheduleState(new HassioState(WeekdayRoutineDevices.PEOPLE_MOM, "home", relativeTime.getTime(), new HassioPersonAttributes()));
        relativeTime.add(Calendar.MINUTE, 50);
        SS.scheduleState(new HassioState(WeekdayRoutineDevices.ROUTINE, "evening", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.MINUTE, 120);
        SS.scheduleState(new HassioState(WeekdayRoutineDevices.ROUTINE, "sleeping", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.MINUTE, 7 * 60);
        SS.scheduleState(new HassioState(WeekdayRoutineDevices.ROUTINE, "morning", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.MINUTE, 45);
        SS.scheduleState(new HassioState(WeekdayRoutineDevices.PEOPLE_DAD, "away", relativeTime.getTime(), new HassioPersonAttributes()));
        relativeTime.add(Calendar.MINUTE, 10);
        SS.scheduleState(new HassioState(WeekdayRoutineDevices.PEOPLE_MOM, "away", relativeTime.getTime(), new HassioPersonAttributes()));
    }
}
