package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.tv.HassioTVAttributes;
import sven.phd.iot.hassio.tv.HassioTVGuideAttributes;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class TVNewsStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(TVDevices.LIVING_TV, "off", startDate, new HassioTVAttributes()));
        DM.logState(new HassioState(TVDevices.LIVING_TV_GUIDE, "Teleshopping", startDate, new HassioTVGuideAttributes()));
    }

    @Override
    public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime) {
        SS.scheduleState(new HassioState(TVDevices.LIVING_TV_GUIDE, "Friends Marathon", relativeTime.getTime(), new HassioTVGuideAttributes("serie")));
        relativeTime.add(Calendar.MINUTE, 150);
        SS.scheduleState(new HassioState(TVDevices.LIVING_TV_GUIDE, "Married at first sight", relativeTime.getTime(), new HassioTVGuideAttributes("serie")));
        relativeTime.add(Calendar.MINUTE, 90);
        SS.scheduleState(new HassioState(TVDevices.LIVING_TV_GUIDE, "The News", relativeTime.getTime(), new HassioTVGuideAttributes("news")));
        relativeTime.add(Calendar.MINUTE, 45);
        SS.scheduleState(new HassioState(TVDevices.LIVING_TV_GUIDE, "The Walking Dead Marathon", relativeTime.getTime(), new HassioTVGuideAttributes("serie")));
        relativeTime.add(Calendar.HOUR, 9);
        SS.scheduleState(new HassioState(TVDevices.LIVING_TV_GUIDE, "The News Morning", relativeTime.getTime(), new HassioTVGuideAttributes("news")));
        relativeTime.add(Calendar.HOUR, 1);
        SS.scheduleState(new HassioState(TVDevices.LIVING_TV_GUIDE, "The Walking Dead Marathon", relativeTime.getTime(), new HassioTVGuideAttributes("serie")));
    }
}
