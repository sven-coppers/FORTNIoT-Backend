package sven.phd.iot.hassio.calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioAttributes;

import java.util.Date;

public class HassioCalendarAttributes extends HassioAttributes {
    @JsonProperty("all_day") boolean allDay;
    @JsonProperty("description") String description;
    @JsonProperty("location") String location;
    @JsonProperty("message") String message;
    @JsonProperty("offset_reached") boolean offsetReached;

    @JsonProperty("end_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    Date endTime;

    @JsonProperty("start_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    Date startTime;

    public HassioCalendarAttributes() {
        // Used by the serializer
    }

    public HassioCalendarAttributes(String message, String location) {
        this.message = message;
        this.location = location;
        this.description = "";
        this.allDay = false;
        this.offsetReached = false;
        this.startTime = null;
        this.endTime = null;
    }
}