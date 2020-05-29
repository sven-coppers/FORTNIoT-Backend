package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.sun.HassioSun;
import sven.phd.iot.scenarios.DeviceSet;

import java.util.ArrayList;

public class VirtualSun extends DeviceSet {
    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        devices.add(new HassioSun());
    }
}
