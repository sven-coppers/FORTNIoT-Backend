package sven.phd.iot.hassio.cleaning;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.sensor.HassioSensorAttributes;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioEvent;

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
    protected List<HassioState> adaptStateToContext(Date newDate, HashMap<String, HassioState> hassioStates) {
        List<HassioState> result = new ArrayList<>();

        HassioState state = hassioStates.get(this.entityID);

        if(state == null || state.state.equals("docked")) return result;

        double timeLeft = ((HassioCleanerAttributes) state.attributes).timeLeft;
        Long deltaTimeInMilliseconds = newDate.getTime() - state.getLastChanged().getTime();
        double deltaTimeInMinutes = ((double) deltaTimeInMilliseconds) / (1000.0 * 60.0);

        timeLeft -= deltaTimeInMinutes;

        if(timeLeft > 0.0) {
            result.add(new HassioState(this.entityID, "cleaning", state.getLastChanged(), new HassioCleanerAttributes(timeLeft)));
        } else {
            result.add(new HassioState(this.entityID, "docked", state.getLastChanged(), new HassioCleanerAttributes(0)));
        }

        return result;
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioCleanerAttributes.class);
    }
}
