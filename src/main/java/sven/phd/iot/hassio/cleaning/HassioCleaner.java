package sven.phd.iot.hassio.cleaning;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.ImplicitBehaviorEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HassioCleaner extends HassioDevice {
    public HassioCleaner(String entityID, String friendlyName) {
        super(entityID, friendlyName);
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioCleanerAttributes.class);
    }

    @Override
    protected List<ImplicitBehaviorEvent> predictImplicitStates(Date newDate, HashMap<String, HassioState> hassioStates) {
        List<ImplicitBehaviorEvent> result = new ArrayList<>();

        HassioState state = hassioStates.get(this.entityID);

        if(state == null || state.state.equals("docked")) return result;

        double timeLeft = ((HassioCleanerAttributes) state.attributes).timeLeft;
        Long deltaTimeInMilliseconds = newDate.getTime() - state.getLastChanged().getTime();
        double deltaTimeInMinutes = ((double) deltaTimeInMilliseconds) / (1000.0 * 60.0);

        timeLeft -= deltaTimeInMinutes;

        if(timeLeft > 0.0) {
            hassioStates.put(this.entityID, new HassioState(this.entityID, "cleaning", state.getLastChanged(), new HassioCleanerAttributes(timeLeft)));
            result.add(new ImplicitBehaviorEvent(newDate, this.entityID));
        } else {
            hassioStates.put(this.entityID, new HassioState(this.entityID, "docked", state.getLastChanged(), new HassioCleanerAttributes(0)));
            result.add(new ImplicitBehaviorEvent(newDate, this.entityID));
        }

        return result;
    }
}