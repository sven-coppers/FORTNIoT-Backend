package sven.phd.iot.hassio.light;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.students.mathias.states.HassioAction;

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
    public List<HassioAction> getAllActions() {
        List<HassioAction> result = new ArrayList<>();
        HassioAction lightOffAction = new HassioAction("LightOffAction", "Turn off light");
        HassioAction lightOnAction = new HassioAction("LightOnAction", "Turn on light");
        result.add(lightOffAction);
        result.add(lightOnAction);

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