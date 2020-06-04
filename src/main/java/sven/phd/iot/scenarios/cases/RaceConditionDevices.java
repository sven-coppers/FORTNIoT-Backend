package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.light.HassioLight;
import sven.phd.iot.hassio.person.HassioPerson;
import sven.phd.iot.hassio.sun.HassioSun;
import sven.phd.iot.scenarios.DeviceSet;

import java.util.ArrayList;

public class RaceConditionDevices extends DeviceSet {
    public static final String KITCHEN_SPOTS                = "light.kitchen_spots";
    public static final String LIVING_SPOTS                 = "light.living_spots";
    public static final String GARDEN_LIGHTS                = "light.garden";
    public static final String PEOPLE_MATHIAS               = "person.mathias";

    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        devices.add(new HassioSun());
        devices.add(new HassioLight(KITCHEN_SPOTS, "Kitchen spots"));
        devices.add(new HassioLight(LIVING_SPOTS, "Living spots"));
        devices.add(new HassioLight(GARDEN_LIGHTS, "lights in the garden"));
        devices.add(new HassioPerson(PEOPLE_MATHIAS, "Mathias"));
    }
}
