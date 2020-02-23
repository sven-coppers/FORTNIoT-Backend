package sven.phd.iot.hassio.person;

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

public class HassioPerson  extends HassioDevice {
    public HassioPerson(String entity_id, String friendlyName) {
        super(entity_id, friendlyName);
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioPersonAttributes.class);
    }

    public List<HassioContext> setState(HassioState hassioState) {
        // We cannot change the state off a person
        return new ArrayList<HassioContext>();
    }

    @Override
    public List<HassioState> getFutureStates() {
        List<HassioState> result = new ArrayList<>();

        return result;
    }

    @Override
    public List<HassioEvent> predictFutureEvents() {
        List<HassioEvent> result = new ArrayList<>();

        return result;
    }
}
