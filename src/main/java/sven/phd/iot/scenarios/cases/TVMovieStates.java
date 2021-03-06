package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.tv.HassioTVAttributes;
import sven.phd.iot.hassio.tv.HassioTVGuideAttributes;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class TVMovieStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(TVDevices.LIVING_TV, "on", startDate, new HassioTVAttributes()));
        DM.logState(new HassioState(TVDevices.LIVING_TV_GUIDE, "News loop", startDate, new HassioTVGuideAttributes("news")));
    }

    @Override
    public void scheduleFutureStates(HassioDeviceManager DM, Calendar relativeTime) {
        DM.scheduleState(new HassioState(TVDevices.LIVING_TV_GUIDE, "Star Wars: The Empire Strikes Back", relativeTime.getTime(), new HassioTVGuideAttributes("movie")));
        relativeTime.add(Calendar.MINUTE, 180);
        DM.scheduleState(new HassioState(TVDevices.LIVING_TV_GUIDE, "Masterchef", relativeTime.getTime(), new HassioTVGuideAttributes("serie")));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(TVDevices.LIVING_TV_GUIDE, "Lord of the Rings Marathon", relativeTime.getTime(), new HassioTVGuideAttributes("movie")));
    }
}
