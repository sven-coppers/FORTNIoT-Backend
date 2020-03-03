package sven.phd.iot.hassio.calendar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.*;
import sven.phd.iot.hassio.updates.HassioEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HassioCalendar extends HassioDevice {
    public HassioCalendar(String entityID, String friendlyName) {
        super(entityID, friendlyName);
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioCalendarAttributes.class);
    }

    @Override
    public List<HassioState> predictFutureStates() {
        List<HassioState> result = new ArrayList<>();
        HassioState hassioCalendarState = this.getLastState();
        HassioCalendarAttributes attributes = (HassioCalendarAttributes) hassioCalendarState.attributes;

        if(hassioCalendarState != null) {
            // TODO
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(attributes.startTime);
            calendar.add(Calendar.HOUR_OF_DAY, -2);
            Date startTime = calendar.getTime();
            calendar.setTime(attributes.endTime);
            calendar.add(Calendar.HOUR_OF_DAY, -2);
            Date endTime = calendar.getTime();

            // If event still needs to start
            if(startTime.compareTo(new Date()) > 0) {
                result.add(new HassioState( this.entityID, "on", startTime, new HassioCalendarAttributes(attributes.message, attributes.location)));
            }

            result.add(new HassioState(this.entityID, "off", endTime, new HassioCalendarAttributes(attributes.message, attributes.location)));
        }

        return result;
    }
}