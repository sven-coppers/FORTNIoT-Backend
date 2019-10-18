package sven.phd.iot.hassio.bus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Calendar;
import java.util.Date;

/** Used for deserialisation only */

public class HassioBusPassage {
    @JsonProperty("direction") String direction;

    @JsonProperty("due_at_realtime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    Date realtime;

    @JsonProperty("due_at_schedule")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    Date scheduled;

    @JsonProperty("due_in_min") int dueInMinutes;
    @JsonProperty("final_destination") String finalDestination;
    @JsonProperty("line_desc") String lineDescription;
    @JsonProperty("line_number") int lineNumber;
    @JsonProperty("line_number_colourBack") String lineNumberColourBack;
    @JsonProperty("line_number_colourBackBorder") String lineNumberColourBackBorder;
    @JsonProperty("line_number_colourBackBorderHex") String lineNumberColourBackBorderHex;
    @JsonProperty("line_number_colourBackHex") String lineNumberColourBackHex;
    @JsonProperty("line_number_colourFront") String lineNumberColourFront;
    @JsonProperty("line_number_colourFrontBorder") String lineNumberColourFrontBorder;
    @JsonProperty("line_number_colourFrontBorderHex") String lineNumberColourFrontBorderHex;
    @JsonProperty("line_number_colourFrontHex") String lineNumberColourFrontHex;
    @JsonProperty("line_number_public") String lineNumberPublic;
    @JsonProperty("line_transport_type") String lineTransportType;
    @JsonProperty("passage") int passage;
    @JsonProperty("stopname") String stopName;

    public void correctTime() {
        Calendar calendar = Calendar.getInstance();

        if(this.realtime != null) {
            calendar.setTime(this.realtime);
            calendar.add(Calendar.HOUR, 2);
            this.realtime = calendar.getTime();
        }

        if(this.scheduled != null) {
            calendar.setTime(scheduled);
            calendar.add(Calendar.HOUR, 2);
            this.scheduled = calendar.getTime();
        }
    }
}
