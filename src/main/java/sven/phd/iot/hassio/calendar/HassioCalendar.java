package sven.phd.iot.hassio.calendar;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;
import sven.phd.iot.hassio.updates.HassioEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HassioCalendar extends HassioDevice {
    public HassioCalendar(String entityID) {
        super(entityID);
    }

    public List<HassioContext> setState(HassioState hassioState) {
        // TODO
        return new ArrayList<HassioContext>();
    }

    @Override
    public String getFriendlyName() {
        HassioCalendarState state = (HassioCalendarState) this.getLastState();
        return state.attributes.friendly_name;
    }

    @Override
    public HassioState processRawState(HassioStateRaw hassioStateRaw) {
        return new HassioCalendarState(hassioStateRaw);
    }

    @Override
    public List<HassioEvent> predictFutureEvents() {
        List<HassioEvent> result = new ArrayList<>();

        return result;
    }

    @Override
    public List<HassioState> predictFutureStates() {
        List<HassioState> result = new ArrayList<>();
        HassioCalendarState hassioCalendarState = (HassioCalendarState) this.getLastState();

        if(hassioCalendarState != null) {
            // TODO
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(hassioCalendarState.attributes.startTime);
            calendar.add(Calendar.HOUR_OF_DAY, -2);
            Date startTime = calendar.getTime();
            calendar.setTime(hassioCalendarState.attributes.endTime);
            calendar.add(Calendar.HOUR_OF_DAY, -2);
            Date endTime = calendar.getTime();

            // If event still needs to start
            if(startTime.compareTo(new Date()) > 0) {
                result.add(new HassioCalendarState("on", this.entityID, startTime, hassioCalendarState.attributes.message, hassioCalendarState.attributes.location));
            }

            result.add(new HassioCalendarState("off", this.entityID, endTime, hassioCalendarState.attributes.message, hassioCalendarState.attributes.location));
        }

        return result;
    }
}