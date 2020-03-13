package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.sun.HassioSun;
import sven.phd.iot.study.StudyDeviceSet;

import java.util.ArrayList;

public class VirtualSun extends StudyDeviceSet {
    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        devices.add(new HassioSun());
    }
}
