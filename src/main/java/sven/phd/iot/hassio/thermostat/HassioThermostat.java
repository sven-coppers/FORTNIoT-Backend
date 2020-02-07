package sven.phd.iot.hassio.thermostat;

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
import java.util.List;

public class HassioThermostat extends HassioDevice {
    public HassioThermostat(String entityID, String friendlyName) {
        super(entityID, friendlyName);
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioThermostatAttributes.class);
    }

    public List<HassioContext> setState(HassioState hassioState) {
        // For now only a virtual device
        return new ArrayList<>();
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
