package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.light.HassioLight;
import sven.phd.iot.study.StudyDeviceSet;

import java.util.ArrayList;

public class LightSimpleDevices extends StudyDeviceSet {
    public static final String LIVING_SPOTS                     = "light.living_spots";

    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        devices.add(new HassioLight(LIVING_SPOTS, "Living spots"));
    }
}
