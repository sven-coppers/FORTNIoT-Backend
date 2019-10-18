package sven.phd.iot.hassio.updates;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;

import java.util.Date;

public class HassioUpdate implements Comparable<HassioUpdate> {
    @JsonProperty("entity_id") public String entity_id;
    @JsonProperty("type") public String type;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("datetime") public Date datetime;

    public HassioUpdate() {
        // Default constructor for serialisation purposes
    }

    public HassioUpdate(String entityID, String type, Date datetime) {
        this.entity_id = entityID;
        this.type = type;
        this.datetime = datetime;
    }

    public int compareTo(HassioUpdate hassioUpdate) {
        return this.datetime.compareTo(hassioUpdate.datetime);
    }
}