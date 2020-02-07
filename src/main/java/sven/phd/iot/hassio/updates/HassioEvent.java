package sven.phd.iot.hassio.updates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;
import java.util.Date;

public class HassioEvent implements Comparable<HassioEvent> {
    @JsonProperty("entity_id") public String entity_id;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("datetime") public Date datetime;

    public HassioEvent(String entityID, Date datetime) {
        this.entity_id = entityID;
        this.datetime = datetime;
    }

    public int compareTo(HassioEvent hassioEvent) {
        if(hassioEvent.datetime == null || this.datetime == null) {
            System.out.println();
        }
        return this.datetime.compareTo(hassioEvent.datetime);
    }
}
