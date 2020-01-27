package sven.phd.iot.hassio.tracker;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;
import sven.phd.iot.hassio.sun.HassioSunState;
import sven.phd.iot.hassio.updates.HassioEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HassioDeviceTracker extends HassioDevice {
    public HassioDeviceTracker(String entityID, String friendlyName) {
        super(entityID, friendlyName);
    }

    public HassioState processRawState(HassioStateRaw hassioStateRaw) {
        return new HassioDeviceTrackerState(hassioStateRaw);
    }

    public List<HassioContext> setState(HassioState hassioState) {
        return new ArrayList<HassioContext>();
    }

    @Override
    public List<HassioState> predictFutureStates() {
        List<HassioState> result = new ArrayList<>();

        // TODO

        Collections.sort(result);

        return result;
    }

    @Override
    public List<HassioEvent> predictFutureEvents() {
        List<HassioEvent> result = new ArrayList<>();

        return result;
    }
}
