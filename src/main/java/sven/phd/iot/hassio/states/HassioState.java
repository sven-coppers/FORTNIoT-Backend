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
}
