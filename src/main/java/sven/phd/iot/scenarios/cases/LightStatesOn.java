package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.light.HassioLightAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class LightStatesOn extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(LightDevices.LIVING_STANDING_LAMP, "on", startDate, new HassioLightAttributes()));
       // DM.logState(new HassioState(LightDevices.KITCHEN_SPOTS, "on", startDate, new HassioLightAttributes()));
        DM.logState(new HassioState(LightDevices.LIVING_LED_STRIPS, "on", startDate, new HassioLightAttributes()));
        DM.logState(new HassioState(LightDevices.LIVING_CHANDELIER, "on", startDate, new HassioLightAttributes()));
    }

    @Override
    public void scheduleFutureStates(HassioDeviceManager DM, Calendar relativeTime) {

    }
}
