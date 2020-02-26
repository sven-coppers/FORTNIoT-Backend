package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.cleaning.HassioCleaner;
import sven.phd.iot.study.StudyDeviceSet;

import java.util.ArrayList;

public class CleaningDevices extends StudyDeviceSet {
    public static final String ROOMBA_DOWNSTAIRS   = "cleaning.roomba_downstairs";
    public static final String ROOMBA_UPSTAIRS     = "cleaning.roomba_upstairs";

    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        devices.add(new HassioCleaner(ROOMBA_DOWNSTAIRS, "Roomba downstairs"));
        devices.add(new HassioCleaner(ROOMBA_UPSTAIRS, "Roomba upstairs"));
    }
}