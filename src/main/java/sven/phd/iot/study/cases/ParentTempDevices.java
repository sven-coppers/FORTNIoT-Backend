package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.climate.*;
import sven.phd.iot.hassio.sensor.HassioIndoorTempSensor;
import sven.phd.iot.hassio.sensor.HassioSensorAttributes;
import sven.phd.iot.study.StudyDeviceSet;

import java.util.ArrayList;

public class ParentTempDevices extends StudyDeviceSet {
    // Master Bedroom - Temperature
    public static final String BEDROOM_MASTER_TEMPERATURE       = "sensor.master_bedroom_temperature_measurement";
    public static final String BEDROOM_MASTER_THERMOSTAT        = "thermostat.master_bedroom_thermostat";
    public static final String BEDROOM_MASTER_RADIATOR          = "heater.master_bedroom_radiator";
    public static final String BEDROOM_MASTER_AIRCO             = "cooler.master_bedroom_airco";

    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        devices.add(new HassioIndoorTempSensor(BEDROOM_MASTER_TEMPERATURE, "Master bedroom temperature", -1.0));
        devices.add(new HassioThermostat(BEDROOM_MASTER_THERMOSTAT, "Master bedroom target temperature"));
        devices.add(new HassioHeater(BEDROOM_MASTER_RADIATOR, "Master bedroom radiator", 3.0, 1.0, BEDROOM_MASTER_THERMOSTAT, BEDROOM_MASTER_TEMPERATURE));
        devices.add(new HassioCooler(BEDROOM_MASTER_AIRCO, "Master Bedroom air conditioning", -3.0, 0.0, BEDROOM_MASTER_THERMOSTAT, BEDROOM_MASTER_TEMPERATURE));
    }

        /*    DM.logState(new HassioState(Entity.BEDROOM_MASTER_RADIATOR, "eco",startDate, new HassioHeaterAttributes()));
        DM.logState(new HassioState(Entity.BEDROOM_MASTER_TEMPERATURE, "17.0",startDate, new HassioSensorAttributes("temperature", "°C")));
        DM.logState(new HassioState(Entity.BEDROOM_MASTER_THERMOSTAT, "17.0",startDate, new HassioThermostatAttributes()));
        DM.logState(new HassioState(Entity.BEDROOM_MASTER_AIRCO, "off",startDate, new HassioCoolerAttributes()));

        DM.logState(new HassioState(Entity.BEDROOM_CHILDREN_RADIATOR, "eco", startDate, new HassioHeaterAttributes()));
        DM.logState(new HassioState(Entity.BEDROOM_CHILDREN_TEMPERATURE, "17.0", startDate, new HassioSensorAttributes("temperature", "°C")));
        DM.logState(new HassioState(Entity.BEDROOM_CHILDREN_THERMOSTAT, "17.0", startDate, new HassioThermostatAttributes()));
        DM.logState(new HassioState(Entity.BEDROOM_CHILDREN_AIRCO, "off", startDate, new HassioCoolerAttributes()));

        DM.logState(new HassioState(Entity.SHOWER_HEATER, "eco", startDate, new HassioHeaterAttributes()));
        DM.logState(new HassioState(Entity.SHOWER_TEMPERATURE, "17.0", startDate, new HassioSensorAttributes("temperature", "°C")));
        DM.logState(new HassioState(Entity.SHOWER_THERMOSTAT, "17.0", startDate, new HassioThermostatAttributes()));
        DM.logState(new HassioState(Entity.SHOWER_VENTILATION, "off", startDate, new HassioCoolerAttributes())); */
}
