package sven.phd.iot.hassio.calendar;

import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;
import sven.phd.iot.hassio.sun.HassioSunAttributes;

import java.io.IOException;
import java.util.Date;

public class HassioCalendarState extends HassioState {
    public HassioCalendarAttributes attributes;

    public HassioCalendarState(HassioStateRaw hassioState) {
        super(hassioState);

        try {
            this.attributes = new ObjectMapper().readValue(hassioState.attributes.toString(), HassioCalendarAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(hassioState.attributes.toString());
        }
    }

    public HassioCalendarState(String state, String entityID, Date date, String message, String location) {
        super(new HassioContext(), entityID, date, date, state);

        this.attributes = new HassioCalendarAttributes(message, location);
    }
}
