package sven.phd.iot.hassio.bus;

import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;
import sven.phd.iot.hassio.sun.HassioSunAttributes;

import java.io.IOException;
import java.util.Date;

public class HassioBusState extends HassioState {
    public HassioBusAttributes attributes;

    public HassioBusState(HassioStateRaw hassioState) {
        super(hassioState);

        try {
            this.attributes = new ObjectMapper().readValue(hassioState.attributes.toString(), HassioBusAttributes.class);

            this.attributes.correctTime(); // De lijn does not set time zone
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(hassioState.attributes.toString());
        }
    }

    // For passages only
    public HassioBusState(String entityID, HassioBusPassage hassioBusPassage) {
        super(
                new HassioContext(),
                entityID,
                (hassioBusPassage.realtime != null ? hassioBusPassage.realtime : hassioBusPassage.scheduled),
                (hassioBusPassage.realtime != null ? hassioBusPassage.realtime : hassioBusPassage.scheduled),
                hassioBusPassage.lineNumberPublic + " " + hassioBusPassage.finalDestination);

        this.attributes = new HassioBusAttributes(hassioBusPassage);
    }
}
