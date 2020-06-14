package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.light.HassioLightAttributes;
import sven.phd.iot.hassio.person.HassioPersonAttributes;
import sven.phd.iot.hassio.routine.HassioRoutineAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.sun.HassioSunAttributes;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class RaceConditionStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(RaceConditionDevices.PEOPLE_MATHIAS, "home", startDate, new HassioPersonAttributes()));
        DM.logState(new HassioState(RaceConditionDevices.LIVING_SPOTS, "on", startDate, new HassioLightAttributes()));
        DM.logState(new HassioState(RaceConditionDevices.KITCHEN_SPOTS, "off", startDate, new HassioLightAttributes()));
        DM.logState(new HassioState(RaceConditionDevices.GARDEN_LIGHTS, "off", startDate, new HassioLightAttributes()));

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
        relativeTime.add(Calendar.MINUTE, 120);
        SS.scheduleState(new HassioState(RaceConditionDevices.PEOPLE_MATHIAS, "away", relativeTime.getTime(), new HassioPersonAttributes()));
    }
}
