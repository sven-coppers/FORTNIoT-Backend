package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.tv.HassioTV;
import sven.phd.iot.hassio.tv.HassioTVGuide;
import sven.phd.iot.study.StudyDeviceSet;

import java.util.ArrayList;

public class TVDevices extends StudyDeviceSet {
    public static final String LIVING_TV = "screen.living_room";
    public static final String LIVING_TV_GUIDE = "routine.tv_guide";

    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        devices.add(new HassioTV(LIVING_TV, "Living television"));
        devices.add(new HassioTVGuide(LIVING_TV_GUIDE, "TV guide"));
    }
}
