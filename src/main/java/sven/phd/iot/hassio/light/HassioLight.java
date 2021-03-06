package sven.phd.iot.hassio.light;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Action;
import sven.phd.iot.rules.actions.LightOffAction;
import sven.phd.iot.rules.actions.LightOnAction;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HassioLight extends HassioDevice {
    public HassioLight(String entityID, String friendlyName) {
        super(entityID, friendlyName);
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioLightAttributes.class);
    }

    @Override
    public List<Action> getAllActions() {
        List<Action> result = new ArrayList<>();
        result.add(new LightOffAction("Turn off " + friendlyName, entityID));
        result.add(new LightOnAction("Turn on " + friendlyName, entityID, Color.YELLOW, false));
        return result;
    }

    public List<HassioContext> setState(HassioState hassioState) {
        if(hassioState.state.equals("on")) {
            return this.callService("light/turn_on", new HassioLightServiceOn(hassioState));
        } else {
            return this.callService("light/turn_off", new HassioLightServiceOff(hassioState));
        }
    }
}