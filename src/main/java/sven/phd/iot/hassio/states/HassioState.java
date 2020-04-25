package sven.phd.iot.hassio.states;

import java.util.Date;

public class HassioState extends HassioAbstractState {
    public HassioAttributes attributes;

    public HassioState(String entityID, String state, Date date, HassioAttributes attributes) {
        super(new HassioContext(entityID, date), entityID, date, date, state);

        this.attributes = attributes;
    }

    public HassioState(HassioStateRaw hassioStateRaw, HassioAttributes attributes) {
        super(hassioStateRaw);

        this.attributes = attributes;
    }

    // Copy Constructor with a new date
    public HassioState(HassioState hassioState, Date date) {
        super(new HassioContext(hassioState.entity_id, date), hassioState.entity_id, date, date, hassioState.state);
        this.attributes = hassioState.attributes;
    }
}
