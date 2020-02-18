package sven.phd.iot.hassio.sensor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HassioSensor extends HassioDevice {
    public HassioSensor(String entityID, String friendlyName) {
        super(entityID, friendlyName);
    }

    public List<HassioContext> setState(HassioState hassioState) {
        // Not sure if we can set the state of a sensor
        return new ArrayList<HassioContext>();
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioSensorAttributes.class);
    }

    @Override
    public List<HassioState> getFutureStates() {
        return new ArrayList<>();
    }

    @Override
    public List<HassioEvent> predictFutureEvents() {
        return new ArrayList<>();
    }
}
