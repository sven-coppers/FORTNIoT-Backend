package sven.phd.iot.hassio.light;

import sven.phd.iot.hassio.states.HassioState;

public class HassioLightServiceOff extends HassioLightService {
    public HassioLightServiceOff(HassioState hassioLightState) {
        this.entity_id = hassioLightState.entity_id;
        this.transition = true;
    }
}
