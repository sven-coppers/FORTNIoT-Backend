package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.blinds.HassioBlind;
import sven.phd.iot.hassio.weather.HassioWeather;
import sven.phd.iot.study.StudyDeviceSet;

import java.util.ArrayList;

public class BlindDevices extends StudyDeviceSet {
    public static final String LIVING_BLINDS = "blinds.living_blinds";
   // public static final String KITCHEN_BLINDS = "blinds.kitchen_blinds";

    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        devices.add(new HassioBlind(LIVING_BLINDS, "Rolling shutter"));
     //   devices.add(new HassioBlind(KITCHEN_BLINDS, "Kitchen blinds"));
    }
}