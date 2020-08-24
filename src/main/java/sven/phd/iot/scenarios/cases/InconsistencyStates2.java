package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.blinds.HassioBlindAttributes;
import sven.phd.iot.hassio.cleaning.HassioCleanerAttributes;
import sven.phd.iot.hassio.light.HassioLightAttributes;
import sven.phd.iot.hassio.lock.HassioLockAttributes;
import sven.phd.iot.hassio.person.HassioPersonAttributes;
import sven.phd.iot.hassio.routine.HassioRoutineAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.sun.HassioSunAttributes;
import sven.phd.iot.hassio.tv.HassioTVAttributes;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class InconsistencyStates2 extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(InconsistencyDevices.PEOPLE_MICHAEL, "away", startDate, new HassioPersonAttributes()));
        DM.logState(new HassioState(InconsistencyDevices.LIVING_BLINDS, "raised", startDate, new HassioBlindAttributes()));
        DM.logState(new HassioState(InconsistencyDevices.ROOMBA_DOWNSTAIRS, "cleaning", startDate, new HassioCleanerAttributes()));
        DM.logState(new HassioState(InconsistencyDevices.GARDEN_LIGHTS, "on", startDate, new HassioLightAttributes()));
        DM.logState(new HassioState(InconsistencyDevices.LIVING_SPOTS, "off", startDate, new HassioLightAttributes()));
        DM.logState(new HassioState(InconsistencyDevices.LIVING_TV, "off", startDate, new HassioTVAttributes()));
        DM.logState(new HassioState(InconsistencyDevices.FRONT_DOOR, "locked", startDate, new HassioLockAttributes()));

        DM.logState(new HassioState(InconsistencyDevices.ROUTINE, "working from home", startDate, new HassioRoutineAttributes()));

        Calendar relativeTime = Calendar.getInstance();
        relativeTime.setTime(startDate);

        HassioSunAttributes sunAttributes = new HassioSunAttributes(30.0f, true);

        relativeTime.add(Calendar.HOUR, 6);
        sunAttributes.nextSetting = relativeTime.getTime();
        relativeTime.add(Calendar.HOUR, 6);
        sunAttributes.nextMidnight = relativeTime.getTime();
        relativeTime.add(Calendar.HOUR, 6);
        sunAttributes.nextRising = relativeTime.getTime();
        relativeTime.add(Calendar.HOUR, 6);
        sunAttributes.nextNoon = relativeTime.getTime();

        DM.logState(new HassioState("sun.sun", "above_horizon", startDate, sunAttributes));
    }

    @Override
    public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime) {
        /*
        relativeTime.add(Calendar.MINUTE, 120);
        SS.scheduleState(new HassioState(InconsistencyDevices.PEOPLE_MICHAEL, "home", relativeTime.getTime(), new HassioPersonAttributes()));
        relativeTime.add(Calendar.MINUTE, 230);
        SS.scheduleState(new HassioState(InconsistencyDevices.ROUTINE, "evening", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.MINUTE, 240);
        SS.scheduleState(new HassioState(InconsistencyDevices.ROUTINE, "sleeping", relativeTime.getTime(), new HassioRoutineAttributes()));
        relativeTime.add(Calendar.MINUTE, 7 * 60);
        SS.scheduleState(new HassioState(InconsistencyDevices.ROUTINE, "morning", relativeTime.getTime(), new HassioRoutineAttributes()));
         */
        relativeTime.add(Calendar.MINUTE, 120);
        SS.scheduleState(new HassioState(InconsistencyDevices.PEOPLE_MICHAEL, "home", relativeTime.getTime(), new HassioPersonAttributes()));
    }
}
