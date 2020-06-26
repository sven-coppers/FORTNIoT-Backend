package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.blinds.HassioBlindAttributes;
import sven.phd.iot.hassio.cleaning.HassioCleanerAttributes;
import sven.phd.iot.hassio.light.HassioLightAttributes;
import sven.phd.iot.hassio.lock.HassioLockAttributes;
import sven.phd.iot.hassio.person.HassioPersonAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.sun.HassioSunAttributes;
import sven.phd.iot.hassio.tv.HassioTVAttributes;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class LoopyStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(InconsistencyDevices.PEOPLE_MATHIAS, "home", startDate, new HassioPersonAttributes()));
        DM.logState(new HassioState(InconsistencyDevices.LIVING_BLINDS, "lowered", startDate, new HassioBlindAttributes()));
        DM.logState(new HassioState(InconsistencyDevices.ROOMBA_DOWNSTAIRS, "off", startDate, new HassioCleanerAttributes()));
        DM.logState(new HassioState(InconsistencyDevices.GARDEN_LIGHTS, "on", startDate, new HassioLightAttributes()));
        DM.logState(new HassioState(InconsistencyDevices.LIVING_SPOTS, "off", startDate, new HassioLightAttributes()));
        DM.logState(new HassioState(InconsistencyDevices.LIVING_TV, "off", startDate, new HassioTVAttributes()));
        DM.logState(new HassioState(InconsistencyDevices.FRONT_DOOR, "locked", startDate, new HassioLockAttributes()));

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
        SS.scheduleState(new HassioState(InconsistencyDevices.PEOPLE_MATHIAS, "away", relativeTime.getTime(), new HassioPersonAttributes()));
    }
}
