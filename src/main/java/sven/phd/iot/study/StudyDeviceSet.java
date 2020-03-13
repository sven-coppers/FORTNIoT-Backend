package sven.phd.iot.study;


import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.HassioDeviceManager;

import java.util.ArrayList;

abstract public class StudyDeviceSet {
    abstract public void createDevices(ArrayList<HassioDevice> devices);
}
