package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.climate.HassioCooler;
import sven.phd.iot.hassio.climate.HassioHeater;
import sven.phd.iot.hassio.climate.HassioThermostat;
import sven.phd.iot.hassio.sensor.HassioIndoorTempSensor;
import sven.phd.iot.study.StudyDeviceSet;

import java.util.ArrayList;
import java.util.List;

public class ShowerTempDevices extends StudyDeviceSet {
    public static final String SHOWER_TEMPERATURE               = "sensor.shower_temperature_measurement";
    public static final String SHOWER_THERMOSTAT                = "thermostat.shower_thermostat";
    public static final String SHOWER_HEATER                    = "heater.shower_heater";
    public static final String SHOWER_VENTILATION               = "cooler.shower_ventilation";

    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        List<String> heaterIDs = new ArrayList<>();
        List<String> coolerIDs = new ArrayList<>();

        heaterIDs.add(SHOWER_HEATER);
        coolerIDs.add(SHOWER_VENTILATION);

        devices.add(new HassioIndoorTempSensor(SHOWER_TEMPERATURE, "Shower room temperature", heaterIDs, coolerIDs, SHOWER_THERMOSTAT, -2.0));
        devices.add(new HassioThermostat(SHOWER_THERMOSTAT, "Shower room target temperature"));
        devices.add(new HassioHeater(SHOWER_HEATER, "Shower room heater fan", 8.0, 2.0, SHOWER_THERMOSTAT, SHOWER_TEMPERATURE));
        devices.add(new HassioCooler(SHOWER_VENTILATION, "Shower room ventilation", -1.0, 0.0, SHOWER_THERMOSTAT, SHOWER_TEMPERATURE));
    }
}
