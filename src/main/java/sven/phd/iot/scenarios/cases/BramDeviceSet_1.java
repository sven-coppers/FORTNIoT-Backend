package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDevice;

import sven.phd.iot.hassio.light.HassioLight;
import sven.phd.iot.hassio.person.HassioPerson;
import sven.phd.iot.hassio.sun.HassioSun;
import sven.phd.iot.scenarios.DeviceSet;

import java.util.ArrayList;

public class BramDeviceSet_1 extends DeviceSet {
    public static final String TABLE_LIGHTS = "light.table_lights";
    public static final String TABLE_LIGHTS_NAME = "Table lights";
    public static final String SUN = "sun.sun";
    public static final String PERSON = "person.bram";
    public static final String PERSON_NAME = "Bram";
    public static final String PERSON_ID = "928fd9bc47ed4a4eacea420fe13de68c";


    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        devices.add(new HassioLight(TABLE_LIGHTS, "Table lights"));
        devices.add(new HassioPerson(PERSON, PERSON_NAME));

    }
}
