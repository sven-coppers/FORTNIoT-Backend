package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.blinds.HassioBlindAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class BlindStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
   //     DM.logState(new HassioState(BlindDevices.KITCHEN_BLINDS, "raised", startDate, new HassioBlindAttributes()));
        DM.logState(new HassioState(BlindDevices.LIVING_BLINDS, "raised", startDate, new HassioBlindAttributes()));
    }

    @Override
    public void scheduleFutureStates(HassioDeviceManager DM, Calendar relativeTime) {

    }
}
