package sven.phd.iot.hassio.light;

public class HassioLightServiceOff extends HassioLightService {
    public HassioLightServiceOff(HassioLightState hassioLightState) {
        this.entity_id = hassioLightState.entity_id;
        this.transition = true;
    }
}
