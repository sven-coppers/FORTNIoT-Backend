package sven.phd.iot.hassio.sun;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.*;

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

    @Override
    public List<HassioState> predictInitialFutureStates() {
        List<HassioState> result = new ArrayList<>();
        result.addAll(this.scheduledStates);

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
}
