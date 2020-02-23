package sven.phd.iot.hassio.tracker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HassioDeviceTracker extends HassioDevice {
    public HassioDeviceTracker(String entityID, String friendlyName) {
        super(entityID, friendlyName);
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioDeviceTrackerAttributes.class);
    }

    public List<HassioContext> setState(HassioState hassioState) {
        return new ArrayList<HassioContext>();
    }

    @Override
    public List<HassioState> getFutureStates() {
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
