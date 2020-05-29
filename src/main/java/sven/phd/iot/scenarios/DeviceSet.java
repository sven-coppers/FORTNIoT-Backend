package sven.phd.iot.scenarios;


import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.HassioDeviceManager;

import java.util.ArrayList;

abstract public class DeviceSet {
    abstract public void createDevices(ArrayList<HassioDevice> devices);
}
