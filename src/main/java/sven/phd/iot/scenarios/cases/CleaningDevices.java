package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.cleaning.HassioCleaner;
import sven.phd.iot.hassio.sensor.HassioBattery;
import sven.phd.iot.scenarios.DeviceSet;

import java.util.ArrayList;

public class CleaningDevices extends DeviceSet {
    public static final String ROOMBA_DOWNSTAIRS            = "cleaning.roomba_downstairs";
    public static final String ROOMBA_DOWNSTAIRS_BATTERY    = "sensor.roomba_downstairs_battery";

    public static final String ROOMBA_UPSTAIRS            = "cleaning.roomba_upstairs";
    public static final String ROOMBA_UPSTAIRS_BATTERY    = "sensor.roomba_upstairs_battery";

    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        devices.add(new HassioCleaner(ROOMBA_DOWNSTAIRS, "Roomba downstairs", ROOMBA_DOWNSTAIRS_BATTERY, 1, 4));
        devices.add(new HassioBattery(ROOMBA_DOWNSTAIRS_BATTERY, "Roomba downstairs battery (%)"));

        devices.add(new HassioCleaner(ROOMBA_UPSTAIRS, "Roomba upstairs", ROOMBA_UPSTAIRS_BATTERY, 2, 5));
        devices.add(new HassioBattery(ROOMBA_UPSTAIRS_BATTERY, "Roomba upstairs battery (%)"));
    }
}