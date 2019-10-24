package sven.phd.iot.hassio.sensor;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;
import sven.phd.iot.hassio.updates.HassioEvent;

import java.util.ArrayList;
import java.util.List;

public class HassioBinarySensor extends HassioDevice {
    public HassioBinarySensor(String entityID) {
        super(entityID);
    }

    public List<HassioContext> setState(HassioState hassioState) {
        // Not sure if we can set the state of a sensor
        return new ArrayList<HassioContext>();
    }

    public HassioState processRawState(HassioStateRaw hassioStateRaw) {
        return new HassioBinarySensorState(hassioStateRaw);
    }

    @Override
    public List<HassioState> predictFutureStates() {
        return null;
    }

    @Override
    public List<HassioEvent> predictFutureEvents() {
        return null;
    }
}
