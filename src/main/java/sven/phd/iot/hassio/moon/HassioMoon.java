package sven.phd.iot.hassio.moon;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioAttributes;

import java.io.IOException;

public class HassioMoon extends HassioDevice {
    public HassioMoon() {
        super("sensor.moon", "Moon");
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioMoonAttributes.class);
    }
}
