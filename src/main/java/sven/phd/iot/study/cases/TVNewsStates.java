package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.tv.HassioTVAttributes;
import sven.phd.iot.hassio.tv.HassioTVGuideAttributes;
import sven.phd.iot.study.StudyStateSet;

import java.util.Calendar;
import java.util.Date;

public class TVNewsStates extends StudyStateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(TVDevices.LIVING_TV, "off", startDate, new HassioTVAttributes()));
        DM.logState(new HassioState(TVDevices.LIVING_TV_GUIDE, "Teleshopping", startDate, new HassioTVGuideAttributes()));
    }

    @Override
    public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime) {
        SS.scheduleState(new HassioState(TVDevices.LIVING_TV_GUIDE, "Friends", relativeTime.getTime(), new HassioTVGuideAttributes("serie")));
        relativeTime.add(Calendar.MINUTE, 45);
        SS.scheduleState(new HassioState(TVDevices.LIVING_TV_GUIDE, "Friends", relativeTime.getTime(), new HassioTVGuideAttributes("serie")));
        relativeTime.add(Calendar.MINUTE, 45);
        SS.scheduleState(new HassioState(TVDevices.LIVING_TV_GUIDE, "The News", relativeTime.getTime(), new HassioTVGuideAttributes("news")));
    }
}
