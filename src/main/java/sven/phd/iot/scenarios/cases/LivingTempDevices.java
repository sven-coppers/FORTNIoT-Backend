package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.climate.HassioCooler;
import sven.phd.iot.hassio.climate.HassioHeater;
import sven.phd.iot.hassio.climate.HassioThermostat;
import sven.phd.iot.hassio.sensor.HassioIndoorTempSensor;
import sven.phd.iot.scenarios.DeviceSet;

import java.util.ArrayList;
import java.util.List;

public class LivingTempDevices extends DeviceSet {
    public static final String LIVING_TEMPERATURE               = "sensor.living_temperature_measurement";
    public static final String LIVING_THERMOSTAT                = "thermostat.living_thermostat";
    public static final String LIVING_FLOOR_HEATING             = "heater.living_floor_heating";
    public static final String LIVING_AIRCO                     = "cooler.living_airco";

    public void createDevices(ArrayList<HassioDevice> devices) {
        List<String> heaterIDs = new ArrayList<>();
        List<String> coolerIDs = new ArrayList<>();

        heaterIDs.add(LIVING_FLOOR_HEATING);
        coolerIDs.add(LIVING_AIRCO);

        devices.add(new HassioIndoorTempSensor(LIVING_TEMPERATURE, "Living temperature", heaterIDs, coolerIDs, LIVING_THERMOSTAT, -0.5));
        devices.add(new HassioThermostat(LIVING_THERMOSTAT, "Living target temperature"));
        devices.add(new HassioHeater(LIVING_FLOOR_HEATING, "Living floor heating", 1.5, 0.5, LIVING_THERMOSTAT, LIVING_TEMPERATURE, true, true));
        devices.add(new HassioCooler(LIVING_AIRCO, "Living air conditioning", -3.0, 0.0, LIVING_THERMOSTAT, LIVING_TEMPERATURE, true, true));
    }
}
