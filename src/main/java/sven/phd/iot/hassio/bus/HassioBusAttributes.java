package sven.phd.iot.hassio.bus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.weather.HassioWeatherForecast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HassioBusAttributes extends HassioAttributes {
    @JsonProperty("attribution") public String attribution;
    @JsonProperty("device_class") public String deviceClass;

    @JsonProperty("due_at_realtime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    Date realtime;

    @JsonProperty("due_at_schedule")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    Date scheduled;

    @JsonProperty("final_destination") String finalDestination;
    @JsonProperty("icon") String icon;
    @JsonProperty("line_number_public") String lineNumberPublic;
    @JsonProperty("line_transport_type") String lineTransportType;
    @JsonProperty("stopname") String stopName;

    @JsonProperty("next_passages") public List<HassioBusPassage> nextPassages;

    public HassioBusAttributes() {
        // Used by the serializer
    }

    public HassioBusAttributes(HassioBusPassage hassioBusPassage) {
        this.attribution = "Data provided by data.delijn.be";
        this.deviceClass = "timestamp";
        this.realtime = hassioBusPassage.realtime;
        this.scheduled = hassioBusPassage.scheduled;
        this.finalDestination = hassioBusPassage.finalDestination;
        this.icon = "mdi:bus";
        this.friendly_name = hassioBusPassage.stopName;
        this.lineNumberPublic = hassioBusPassage.lineNumberPublic;
        this.lineTransportType = "BUS";
        this.nextPassages = new ArrayList<>();
    }

    public void correctTime() {
        Calendar calendar = Calendar.getInstance();

        if(this.realtime != null) {
            calendar.setTime(this.realtime);
            calendar.add(Calendar.HOUR, 2);
            this.realtime = calendar.getTime();
        }

        if(this.scheduled != null) {
            calendar.setTime(this.scheduled);
            calendar.add(Calendar.HOUR, 2);
            this.scheduled = calendar.getTime();
        }

        for(HassioBusPassage hassioBusPassage : this.nextPassages) {
          //  hassioBusPassage.correctTime();
        }
    }
}
