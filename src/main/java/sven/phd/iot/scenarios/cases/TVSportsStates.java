package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.tv.HassioTVAttributes;
import sven.phd.iot.hassio.tv.HassioTVGuideAttributes;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class TVSportsStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(TVDevices.LIVING_TV, "on", startDate, new HassioTVAttributes()));
        DM.logState(new HassioState(TVDevices.LIVING_TV_GUIDE, "News loop", startDate, new HassioTVGuideAttributes("news")));
    }

    @Override
    public void scheduleFutureStates(HassioDeviceManager DM, Calendar relativeTime) {
        relativeTime.add(Calendar.MINUTE, 20);
        DM.scheduleState(new HassioState(TVDevices.LIVING_TV_GUIDE, "Superbowl", relativeTime.getTime(), new HassioTVGuideAttributes("sports")));
        relativeTime.add(Calendar.MINUTE, 240);
        DM.scheduleState(new HassioState(TVDevices.LIVING_TV_GUIDE, "Friends Marathon", relativeTime.getTime(), new HassioTVGuideAttributes("serie")));
    }
}
