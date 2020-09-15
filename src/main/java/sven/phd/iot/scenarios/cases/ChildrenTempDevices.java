package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.climate.HassioCooler;
import sven.phd.iot.hassio.climate.HassioHeater;
import sven.phd.iot.hassio.climate.HassioThermostat;
import sven.phd.iot.hassio.sensor.HassioBinarySensor;
import sven.phd.iot.hassio.sensor.HassioIndoorTempSensor;
import sven.phd.iot.scenarios.DeviceSet;

import java.util.ArrayList;
import java.util.List;

public class ChildrenTempDevices extends DeviceSet {
    public static final String BEDROOM_CHILDREN_TEMPERATURE     = "sensor.children_bedroom_temperature_measurement";
    public static final String BEDROOM_CHILDREN_THERMOSTAT      = "thermostat.children_bedroom_thermostat";
    public static final String BEDROOM_CHILDREN_RADIATOR        = "heater.children_bedroom_radiator";
    public static final String BEDROOM_CHILDREN_AIRCO           = "cooler.children_bedroom_airco";
    public static final String BEDROOM_CHILDREN_MOTION          = "binary_sensor.children_bedroom_motion_sensor_motion";

    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        List<String> heaterIDs = new ArrayList<>();
        List<String> coolerIDs = new ArrayList<>();

        heaterIDs.add(BEDROOM_CHILDREN_RADIATOR);
        coolerIDs.add(BEDROOM_CHILDREN_AIRCO);

        devices.add(new HassioIndoorTempSensor(BEDROOM_CHILDREN_TEMPERATURE, "Children's bedroom temperature", heaterIDs, coolerIDs, BEDROOM_CHILDREN_THERMOSTAT, -1.0));
        devices.add(new HassioThermostat(BEDROOM_CHILDREN_THERMOSTAT, "Children's bedroom target temperature"));
        devices.add(new HassioHeater(BEDROOM_CHILDREN_RADIATOR, "Children's bedroom radiator", 4.0, 1.0, BEDROOM_CHILDREN_THERMOSTAT, BEDROOM_CHILDREN_TEMPERATURE, true, true));
        devices.add(new HassioCooler(BEDROOM_CHILDREN_AIRCO, "Children's bedroom air conditioning", -3.0, 0.0, BEDROOM_CHILDREN_THERMOSTAT, BEDROOM_CHILDREN_TEMPERATURE, true, true));
        devices.add(new HassioBinarySensor(BEDROOM_CHILDREN_MOTION, "Children's bedroom motion sensor"));
    }
}
