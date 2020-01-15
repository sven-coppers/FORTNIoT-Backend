package sven.phd.iot.hassio.outlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.light.HassioLightAttributes;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;

import java.io.IOException;
import java.util.Date;

public class HassioOutletState extends HassioState {
    public HassioOutletAttributes attributes;

    public HassioOutletState(String entity_id, String state, Date date) {
        super(new HassioContext(), entity_id, date, date, state);
        this.attributes = new HassioOutletAttributes();
    }

    public HassioOutletState(HassioStateRaw hassioState) {
        super(hassioState);

        try {
            this.attributes = new ObjectMapper().readValue(hassioState.attributes.toString(), HassioOutletAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(hassioState.attributes.toString());
        }
    }


    @Override
    public HassioState processRawState(HassioStateRaw raw) {
        return new HassioOutletState(raw);
    }
}
