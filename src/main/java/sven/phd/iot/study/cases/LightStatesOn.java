package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.light.HassioLightAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.study.StudyStateSet;

import java.util.Calendar;
import java.util.Date;

public class LightStatesOn extends StudyStateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(LightDevices.LIVING_STANDING_LAMP, "on", startDate, new HassioLightAttributes()));
       // DM.logState(new HassioState(LightDevices.KITCHEN_SPOTS, "on", startDate, new HassioLightAttributes()));
        DM.logState(new HassioState(LightDevices.LIVING_LED_STRIPS, "on", startDate, new HassioLightAttributes()));
        DM.logState(new HassioState(LightDevices.LIVING_CHANDELIER, "on", startDate, new HassioLightAttributes()));
    }

    @Override
    public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime) {

    }
}
