package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.lock.HassioLock;
import sven.phd.iot.scenarios.DeviceSet;

import java.util.ArrayList;

public class SecurityDevices extends DeviceSet {
    public static final String FRONT_DOOR = "lock.front_door";
    public static final String BACK_DOOR = "lock.back_door";
    public static final String GARAGE_DOOR = "lock.garage_door";
    public static final String LIVING_MOTION = "binary_sensor.living_motion_sensor_motion";
    public static final String SIRENE = "sirene.sirene";

    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        devices.add(new HassioLock(FRONT_DOOR, "Front door"));
        devices.add(new HassioLock(BACK_DOOR, "Back door"));
        devices.add(new HassioLock(GARAGE_DOOR, "Garage door"));
        devices.add(new HassioLock(LIVING_MOTION, "Living motion sensor"));
        devices.add(new HassioLock(SIRENE, "Alarm siren"));
    }
}
