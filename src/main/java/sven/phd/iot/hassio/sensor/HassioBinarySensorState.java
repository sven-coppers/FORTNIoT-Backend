package sven.phd.iot.hassio.sensor;

import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;

import java.io.IOException;

public class HassioBinarySensorState extends HassioState {
    public HassioBinarySensorAttributes attributes;

    public HassioBinarySensorState(HassioStateRaw hassioState) {
        super(hassioState);

        try {
            this.attributes = new ObjectMapper().readValue(hassioState.attributes.toString(), HassioBinarySensorAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(hassioState.attributes.toString());
        }
    }

    @Override
    public HassioState processRawState(HassioStateRaw raw) {
        return new HassioBinarySensorState(raw);
    }
}
