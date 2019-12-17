package sven.phd.iot.hassio.light;

import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.states.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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
    public HassioConflictState compareAttributes(HassioState state) {
        HassioConflictState conflictState = super.compareAttributes(state);
        try{
            HassioLightState lightState = (HassioLightState) state;
            List<HassioConflictingAttribute> conflictingAttributes = this.attributes.checkForConflicts(lightState.attributes);
            if (!conflictingAttributes.isEmpty()) {
                if (conflictState == null) {
                    conflictState = new HassioConflictState(this.entity_id, this.type, this.datetime);
                }
                conflictState.conflicts.addAll(conflictingAttributes);
                return  conflictState;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }
}