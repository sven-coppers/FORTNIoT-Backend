package sven.phd.iot.hassio.climate;

import sven.phd.iot.hassio.HassioDevice;

abstract public class HassioTemperatureModifier extends HassioDevice {
    protected final String thermostatID;
    protected final String tempSensorID;

    public HassioTemperatureModifier(String entityID, String friendlyName, String thermostatID, String tempSensorID) {
        super(entityID, friendlyName);
        this.thermostatID = thermostatID;
        this.tempSensorID = tempSensorID;
    }
}
