package sven.phd.iot.hassio.sensor;

import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;

import java.io.IOException;

public class HassioSensorState extends HassioState {
    public HassioSensorAttributes attributes;

    public HassioSensorState(HassioStateRaw hassioState) {
        super(hassioState);

        try {
            this.attributes = new ObjectMapper().readValue(hassioState.attributes.toString(), HassioSensorAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public HassioState processRawState(HassioStateRaw raw) {
        return new HassioSensorState(raw);
    }
}
