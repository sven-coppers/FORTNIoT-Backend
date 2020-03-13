package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.cleaning.HassioCleaner;
import sven.phd.iot.hassio.sensor.HassioBattery;
import sven.phd.iot.hassio.sensor.HassioSensor;
import sven.phd.iot.study.StudyDeviceSet;

import java.util.ArrayList;

public class CleaningDevices extends StudyDeviceSet {
    public static final String ROOMBA_DOWNSTAIRS            = "cleaning.roomba_downstairs";
    public static final String ROOMBA_DOWNSTAIRS_BATTERY    = "sensor.roomba_downstairs_battery";

    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        devices.add(new HassioCleaner(ROOMBA_DOWNSTAIRS, "Roomba", ROOMBA_DOWNSTAIRS_BATTERY));
        devices.add(new HassioBattery(ROOMBA_DOWNSTAIRS_BATTERY, "Roomba Battery (%)", 5));
    }
}