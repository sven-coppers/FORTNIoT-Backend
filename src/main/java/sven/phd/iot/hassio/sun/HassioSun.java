package sven.phd.iot.hassio.sun;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;
import sven.phd.iot.hassio.tracker.HassioDeviceTrackerState;
import sven.phd.iot.hassio.updates.HassioEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HassioSun extends HassioDevice {
    public HassioSun() {
        super("sun.sun");
    }

    public HassioState processRawState(HassioStateRaw hassioStateRaw) {
        return new HassioSunState(hassioStateRaw);
    }

    public List<HassioContext> setState(HassioState hassioState) {
        // We cannot change the state off the sun
        return new ArrayList<HassioContext>();
    }

    @Override
    public String getFriendlyName() {
        HassioSunState state = (HassioSunState) this.getLastState();
        return state.attributes.friendly_name;
    }

    @Override
    public List<HassioState> predictFutureStates() {
        List<HassioState> result = new ArrayList<>();
        HassioSunState hassioSunState = (HassioSunState) this.getLastState();

        if(hassioSunState != null) {
            // Next sun rise
            result.add(new HassioSunState("above_horizon", hassioSunState.attributes.nextRising, 0.0f, true));

            // Next sun set
            result.add(new HassioSunState("below_horizon", hassioSunState.attributes.nextSetting, 0.0f, true));

            // Next midnight
            result.add(new HassioSunState("below_horizon", hassioSunState.attributes.nextMidnight, -18.48f, true));

            // Next noon
            result.add(new HassioSunState("above_horizon", hassioSunState.attributes.nextNoon, 48.39f, false));
        }

        Collections.sort(result);

        return result;
    }

    @Override
    public List<HassioEvent> predictFutureEvents() {
        List<HassioEvent> result = new ArrayList<>();

        return result;
    }

}
