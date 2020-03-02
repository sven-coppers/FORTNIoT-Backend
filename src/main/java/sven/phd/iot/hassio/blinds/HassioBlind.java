package sven.phd.iot.hassio.blinds;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HassioBlind extends HassioDevice {
    public HassioBlind(String entityID, String friendlyName) {
        super(entityID, friendlyName);
    }

    @Override
    public List<HassioContext> setState(HassioState hassioState) {
        return new ArrayList<>();
    }

    @Override
    protected List<HassioState> getFutureStates() {
        return new ArrayList<>();
    }

    @Override
    public List<HassioEvent> predictFutureEvents() {
        return new ArrayList<>();
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioBlindAttributes.class);
    }
}
