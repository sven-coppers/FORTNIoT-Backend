package sven.phd.iot.hassio.person;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioAttributes;

import java.io.IOException;

public class HassioPerson  extends HassioDevice {
    public HassioPerson(String entity_id, String friendlyName) {
        super(entity_id, friendlyName);
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioPersonAttributes.class);
    }
}
