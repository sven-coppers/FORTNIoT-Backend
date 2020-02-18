package sven.phd.iot.hassio.outlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.services.HassioService;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioAbstractState;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HassioOutlet extends HassioDevice {
    public HassioOutlet(String entityID, String friendlyName) {
        super(entityID, friendlyName);
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioOutletAttributes.class);
    }

    public List<HassioContext> setState(HassioState hassioState) {
        if(hassioState.state.equals("on")) {
            return this.callService("switch/turn_on", new HassioService(this.entityID));
        } else {
            return this.callService("switch/turn_off", new HassioService(this.entityID));
        }
    }

    @Override
    public List<HassioState> getFutureStates() {
        return new ArrayList<>();
    }

    @Override
    public List<HassioEvent> predictFutureEvents() {
        List<HassioEvent> result = new ArrayList<>();

        return result;
    }
}