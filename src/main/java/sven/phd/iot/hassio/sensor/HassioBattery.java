package sven.phd.iot.hassio.sensor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioEvent;
import sven.phd.iot.hassio.updates.ImplicitBehaviorEvent;

import java.io.IOException;
import java.util.*;

public class HassioBattery extends HassioSensor {
    public HassioBattery(String entityID, String friendlyName) {
        super(entityID, friendlyName);
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioBatteryAttributes.class);
    }
}
