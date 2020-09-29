package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.lock.HassioLockAttributes;
import sven.phd.iot.hassio.sensor.HassioBinarySensorAttributes;
import sven.phd.iot.hassio.sirene.HassioSireneAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class SecurityStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(SecurityDevices.FRONT_DOOR, "locked", startDate, new HassioLockAttributes()));
        DM.logState(new HassioState(SecurityDevices.BACK_DOOR, "locked", startDate, new HassioLockAttributes()));
        DM.logState(new HassioState(SecurityDevices.GARAGE_DOOR, "locked", startDate, new HassioLockAttributes()));
        DM.logState(new HassioState(SecurityDevices.LIVING_MOTION, "clear", startDate, new HassioBinarySensorAttributes()));
        DM.logState(new HassioState(SecurityDevices.SIRENE, "idle", startDate, new HassioSireneAttributes()));
    }

    @Override
    public void scheduleFutureStates(HassioDeviceManager DM, Calendar relativeTime) {

    }
}
