package sven.phd.iot.hassio.climate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.states.HassioAttributes;

import java.io.IOException;

public class HassioCooler extends HassioTemperatureModifier {
    public HassioCooler(String entityID, String friendlyName, String thermostatID, String tempSensorID) {
        super(entityID, friendlyName, thermostatID, tempSensorID);
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioCoolerAttributes.class);
    }
}

