package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.climate.HassioCooler;
import sven.phd.iot.hassio.climate.HassioHeater;
import sven.phd.iot.hassio.climate.HassioThermostat;
import sven.phd.iot.hassio.sensor.HassioIndoorTempSensor;
import sven.phd.iot.scenarios.DeviceSet;

import java.util.ArrayList;
import java.util.List;

public class BedroomTempDevices extends DeviceSet {
    public static final String BEDROOM_TEMPERATURE               = "sensor.bedroom_temperature_measurement";
    public static final String BEDROOM_THERMOSTAT                = "thermostat.bedroom_thermostat";
    public static final String BEDROOM_HEATING                   = "heater.bedroom_heating";
    public static final String BEDROOM_AIRCO                     = "cooler.bedroom_airco";

    public void createDevices(ArrayList<HassioDevice> devices) {
        List<String> heaterIDs = new ArrayList<>();
        List<String> coolerIDs = new ArrayList<>();

        heaterIDs.add(BEDROOM_HEATING);
        coolerIDs.add(BEDROOM_AIRCO);

        devices.add(new HassioIndoorTempSensor(BEDROOM_TEMPERATURE, "Bedroom temperature", heaterIDs, coolerIDs, BEDROOM_THERMOSTAT, -0.1));
      //  devices.add(new HassioThermostat(BEDROOM_THERMOSTAT, "Bedroom target temperature"));
        devices.add(new HassioHeater(BEDROOM_HEATING, "Bedroom heater", 2.0, 0.5, BEDROOM_THERMOSTAT, BEDROOM_TEMPERATURE));
        devices.add(new HassioCooler(BEDROOM_AIRCO, "Bedroom air conditioning", -1.0, 0.0, BEDROOM_THERMOSTAT, BEDROOM_TEMPERATURE));
    }
}
