package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
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

public class RedundancyStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(RedundancyDevices.PEOPLE_MICHAEL, "home", startDate, new HassioPersonAttributes()));
        DM.logState(new HassioState(RedundancyDevices.LIVING_BLINDS, "raised", startDate, new HassioBlindAttributes()));
        DM.logState(new HassioState(RedundancyDevices.ROOMBA_DOWNSTAIRS, "off", startDate, new HassioCleanerAttributes()));
        DM.logState(new HassioState(RedundancyDevices.GARDEN_LIGHTS, "off", startDate, new HassioLightAttributes()));
        DM.logState(new HassioState(RedundancyDevices.LIVING_SPOTS, "off", startDate, new HassioLightAttributes()));
        DM.logState(new HassioState(RedundancyDevices.LIVING_TV, "off", startDate, new HassioTVAttributes()));
        DM.logState(new HassioState(RedundancyDevices.FRONT_DOOR, "locked", startDate, new HassioLockAttributes()));

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
    public void scheduleFutureStates(HassioDeviceManager DM, Calendar relativeTime) {
        relativeTime.add(Calendar.HOUR, 12);
        DM.scheduleState(new HassioState(RedundancyDevices.PEOPLE_MICHAEL, "home", relativeTime.getTime(), new HassioPersonAttributes()));
    }
}
