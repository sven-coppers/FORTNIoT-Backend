package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.light.HassioLight;
import sven.phd.iot.study.StudyDeviceSet;

import java.util.ArrayList;

public class LightDevices extends StudyDeviceSet {
    public static final String LIVING_STANDING_LAMP             = "light.living_standing_lamp";
    public static final String LIVING_CHANDELIER                = "light.living_chandelier";
    public static final String LIVING_LED_STRIPS                = "light.living_dimmables";
   // public static final String KITCHEN_SPOTS                    = "light.kitchen_spots";

    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        devices.add(new HassioLight(LIVING_STANDING_LAMP, "Living floor lamp"));
      //  devices.add(new HassioLight(KITCHEN_SPOTS, "Kitchen spots"));
        devices.add(new HassioLight(LIVING_CHANDELIER, "Living chandelier"));
        devices.add(new HassioLight(LIVING_LED_STRIPS, "Living led strips"));
    }
}
