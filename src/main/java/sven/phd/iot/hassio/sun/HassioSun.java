package sven.phd.iot.hassio.sun;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.*;
import sven.phd.iot.hassio.updates.HassioEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HassioSun extends HassioDevice {
    public HassioSun() {
        super("sun.sun", "Sun");
    }

    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioSunAttributes.class);
    }

    public List<HassioContext> setState(HassioState hassioState) {
        // We cannot change the state off the sun
        return new ArrayList<HassioContext>();
    }

    @Override
    public List<HassioState> getFutureStates() {
        List<HassioState> result = new ArrayList<>();

        HassioState state = this.getLastState();

        if(state != null) {
            HassioSunAttributes attributes = (HassioSunAttributes) state.attributes;

            // Next sun rise
            result.add(new HassioState(this.entityID, "above_horizon", attributes.nextRising, new HassioSunAttributes(0.0f, true)));

            // Next sun set
            result.add(new HassioState(this.entityID, "below_horizon", attributes.nextSetting, new HassioSunAttributes(0.0f, true)));

            // Next midnight
            //result.add(new HassioState(this.entityID, "below_horizon", attributes.nextMidnight, new HassioSunAttributes(-38.48f, true)));

            // Next noon
           //result.add(new HassioState(this.entityID, "above_horizon", attributes.nextNoon, new HassioSunAttributes(48.39f, false)));
        }

        Collections.sort(result);

        return result;
    }

    @Override
    public List<HassioEvent> predictFutureEvents() {
        List<HassioEvent> result = new ArrayList<>();

        return result;
    }
}
