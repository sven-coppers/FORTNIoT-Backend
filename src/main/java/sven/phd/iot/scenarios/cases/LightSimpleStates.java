package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.light.HassioLightAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class LightSimpleStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(LightSimpleDevices.LIVING_SPOTS, "off", startDate, new HassioLightAttributes()));
    }

    @Override
    public void scheduleFutureStates(HassioDeviceManager DM, Calendar relativeTime) {

    }
}
