package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.light.HassioLightAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.study.StudyStateSet;

import java.util.Calendar;
import java.util.Date;

public class LightStates extends StudyStateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(LightDevices.LIVING_STANDING_LAMP, "off", startDate, new HassioLightAttributes()));
        DM.logState(new HassioState(LightDevices.LIVING_SPOTS, "off", startDate, new HassioLightAttributes()));
        DM.logState(new HassioState(LightDevices.KITCHEN_SPOTS, "off", startDate, new HassioLightAttributes()));
        DM.logState(new HassioState(LightDevices.LIVING_LED_STRIPS, "off", startDate, new HassioLightAttributes()));
        DM.logState(new HassioState(LightDevices.LIVING_CHANDELIER, "off", startDate, new HassioLightAttributes()));
    }

    @Override
    public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime) {

    }
}
