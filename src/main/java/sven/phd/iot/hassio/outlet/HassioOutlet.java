package sven.phd.iot.hassio.outlet;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.light.HassioLightServiceOff;
import sven.phd.iot.hassio.light.HassioLightServiceOn;
import sven.phd.iot.hassio.light.HassioLightState;
import sven.phd.iot.hassio.services.HassioService;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;
import sven.phd.iot.hassio.tracker.HassioDeviceTrackerState;
import sven.phd.iot.hassio.updates.HassioEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HassioOutlet extends HassioDevice {
    public HassioOutlet(String entityID) {
        super(entityID);
    }

    public HassioState processRawState(HassioStateRaw hassioStateRaw) {
        return new HassioOutletState(hassioStateRaw);
    }

    public List<HassioContext> setState(HassioState hassioState) {
        if(hassioState.state.equals("on")) {
            return this.callService("switch/turn_on", new HassioService(this.entityID));
        } else {
            return this.callService("switch/turn_off", new HassioService(this.entityID));
        }
    }

    @Override
    public String getFriendlyName() {
        HassioOutletState state = (HassioOutletState) this.getLastState();
        return state.attributes.friendly_name;
    }

    @Override
    public List<HassioState> predictFutureStates() {
        return new ArrayList<>();
    }

    @Override
    public List<HassioEvent> predictFutureEvents() {
        List<HassioEvent> result = new ArrayList<>();

        return result;
    }
}