package sven.phd.iot.hassio.thermostat;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.light.HassioLightServiceOff;
import sven.phd.iot.hassio.light.HassioLightServiceOn;
import sven.phd.iot.hassio.light.HassioLightState;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;
import sven.phd.iot.hassio.updates.HassioEvent;

import java.util.ArrayList;
import java.util.List;

public class HassioThermostat extends HassioDevice {
    public HassioThermostat(String entityID) {
        super(entityID);
    }

    public HassioState processRawState(HassioStateRaw hassioStateRaw) {
        return new HassioLightState(hassioStateRaw);
    }

    public List<HassioContext> setState(HassioState hassioState) {
        // For now only a virtual device
        return new ArrayList<>();
    }

    @Override
    public String getFriendlyName() {
        HassioThermostatState state = (HassioThermostatState) this.getLastState();
        return state.attributes.friendly_name;
    }

    @Override
    public List<HassioState> predictFutureStates() {
        // A lamp cannot know its future state
        return new ArrayList<HassioState>();
    }

    @Override
    public List<HassioEvent> predictFutureEvents() {
        // A lamp cannot know its future events
        return new ArrayList<HassioEvent>();
    }
}
