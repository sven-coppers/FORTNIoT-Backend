package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.sensor.HassioSensor;
import sven.phd.iot.study.StudyDeviceSet;

import java.util.ArrayList;

public class SmokeDevices extends StudyDeviceSet {
    public final static String SENSOR_LIVING_SMOKE = "sensor.smoke";

    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        devices.add(new HassioSensor(SENSOR_LIVING_SMOKE, "Living smoke detector"));
    }
}
