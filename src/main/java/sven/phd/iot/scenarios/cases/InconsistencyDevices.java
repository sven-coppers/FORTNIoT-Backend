package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.blinds.HassioBlind;
import sven.phd.iot.hassio.cleaning.HassioCleaner;
import sven.phd.iot.hassio.light.HassioLight;
import sven.phd.iot.hassio.lock.HassioLock;
import sven.phd.iot.hassio.person.HassioPerson;
import sven.phd.iot.hassio.routine.HassioRoutine;
import sven.phd.iot.hassio.sensor.HassioBattery;
import sven.phd.iot.hassio.sun.HassioSun;
import sven.phd.iot.hassio.tv.HassioTV;
import sven.phd.iot.scenarios.DeviceSet;

import java.util.ArrayList;

public class InconsistencyDevices extends DeviceSet {
    public static final String LIVING_SPOTS                 = "light.living_spots";
    public static final String GARDEN_LIGHTS                = "light.garden";
    public static final String PEOPLE_MATHIAS               = "person.mathias";
    public static final String PEOPLE_MICHAEL               = "person.michael";
    public static final String LIVING_BLINDS                = "blinds.living_blinds";
    public static final String ROOMBA_DOWNSTAIRS            = "cleaning.roomba_downstairs";
    public static final String ROOMBA_DOWNSTAIRS_BATTERY    = "sensor.roomba_downstairs_battery";
    public static final String LIVING_TV                    = "screen.living_room";
    public static final String FRONT_DOOR                   = "lock.front_door";
    public static final String ROUTINE                      = "routine.friends_routine";

    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        devices.add(new HassioSun());
        devices.add(new HassioPerson(PEOPLE_MATHIAS, "Mathias"));
        devices.add(new HassioPerson(PEOPLE_MICHAEL, "Michael"));
        devices.add(new HassioBlind(LIVING_BLINDS, "Rolling shutter"));
        devices.add(new HassioCleaner(ROOMBA_DOWNSTAIRS, "Roomba downstairs", ROOMBA_DOWNSTAIRS_BATTERY, 1, 4));
        devices.add(new HassioBattery(ROOMBA_DOWNSTAIRS_BATTERY, "Roomba downstairs battery (%)"));
        devices.add(new HassioLight(LIVING_SPOTS, "Living spots"));
        devices.add(new HassioLight(GARDEN_LIGHTS, "lights in the garden"));
        devices.add(new HassioTV(LIVING_TV, "Living television"));
        devices.add(new HassioLock(FRONT_DOOR, "Front door"));
        devices.add(new HassioRoutine(ROUTINE, "Routine"));
    }
}
