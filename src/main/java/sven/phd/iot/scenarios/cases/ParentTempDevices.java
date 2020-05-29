package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.climate.*;
import sven.phd.iot.hassio.sensor.HassioIndoorTempSensor;
import sven.phd.iot.scenarios.DeviceSet;

import java.util.ArrayList;
import java.util.List;

public class ParentTempDevices extends DeviceSet {
    // Master Bedroom - Temperature
    public static final String BEDROOM_MASTER_TEMPERATURE       = "sensor.master_bedroom_temperature_measurement";
    public static final String BEDROOM_MASTER_THERMOSTAT        = "thermostat.master_bedroom_thermostat";
    public static final String BEDROOM_MASTER_RADIATOR          = "heater.master_bedroom_radiator";
    public static final String BEDROOM_MASTER_AIRCO             = "cooler.master_bedroom_airco";

    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        List<String> heaterIDs = new ArrayList<>();
        List<String> coolerIDs = new ArrayList<>();

        heaterIDs.add(BEDROOM_MASTER_RADIATOR);
        coolerIDs.add(BEDROOM_MASTER_AIRCO);

        devices.add(new HassioIndoorTempSensor(BEDROOM_MASTER_TEMPERATURE, "Master bedroom temperature", heaterIDs, coolerIDs, BEDROOM_MASTER_THERMOSTAT, -1.0));
        devices.add(new HassioThermostat(BEDROOM_MASTER_THERMOSTAT, "Master bedroom target temperature"));
        devices.add(new HassioHeater(BEDROOM_MASTER_RADIATOR, "Master bedroom radiator", 3.0, 1.0, BEDROOM_MASTER_THERMOSTAT, BEDROOM_MASTER_TEMPERATURE));
        devices.add(new HassioCooler(BEDROOM_MASTER_AIRCO, "Master Bedroom air conditioning", -3.0, 0.0, BEDROOM_MASTER_THERMOSTAT, BEDROOM_MASTER_TEMPERATURE));
    }
}
