package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.person.HassioPerson;
import sven.phd.iot.hassio.routine.HassioRoutine;
import sven.phd.iot.hassio.sensor.HassioSensor;
import sven.phd.iot.scenarios.DeviceSet;

import java.util.ArrayList;

public class WeekdayRoutineDevices extends DeviceSet {
    public static final String PEOPLE_MOM                      = "person.mom";
    public static final String PEOPLE_DAD                      = "person.dad";
    public static final String PEOPLE_HOME_COUNTER             = "sensor.people_home_counter";
    public static final String ROUTINE                         = "routine.family_routine";

    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        devices.add(new HassioPerson(PEOPLE_DAD, "Daddy"));
        devices.add(new HassioPerson(PEOPLE_MOM, "Mommy"));
     //   devices.add(new HassioSensor(PEOPLE_HOME_COUNTER, "Family members home"));
        devices.add(new HassioRoutine(ROUTINE, "Routine"));
    }
}
