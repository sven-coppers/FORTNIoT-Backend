package sven.phd.iot.hassio.light;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;
import sven.phd.iot.hassio.updates.HassioEvent;

import java.util.ArrayList;
import java.util.List;

public class HassioLight extends HassioDevice {
    public HassioLight(String entityID, String friendlyName) {
        super(entityID, friendlyName);
    }

    public HassioState processRawState(HassioStateRaw hassioStateRaw) {
        return new HassioLightState(hassioStateRaw);
    }

    public List<HassioContext> setState(HassioState hassioState) {
        if(hassioState.state.equals("on")) {
            return this.callService("light/turn_on", new HassioLightServiceOn((HassioLightState) hassioState));
        } else {
            return this.callService("light/turn_off", new HassioLightServiceOff((HassioLightState) hassioState));
        }
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