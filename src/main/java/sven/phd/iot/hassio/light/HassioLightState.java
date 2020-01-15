package sven.phd.iot.hassio.light;

import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;

import java.awt.*;
import java.io.IOException;
import java.util.Date;

public class HassioLightState extends HassioState {
    public HassioLightAttributes attributes;

    public HassioLightState(String entity_id, String state, Date date) {
        super(new HassioContext(), entity_id, date, date, state);
        this.attributes = new HassioLightAttributes();
    }

    public HassioLightState(HassioStateRaw hassioState) {
        super(hassioState);

        try {
            this.attributes = new ObjectMapper().readValue(hassioState.attributes.toString(), HassioLightAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HassioState processRawState(HassioStateRaw raw) {
        return new HassioLightState(raw);
    }
}