package sven.phd.iot.hassio.states;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HassioConflictState {
    @JsonProperty("entity_id") public String entity_id;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("datetime") public Date datetime;

    @JsonProperty("type") public String type;
    @JsonProperty("state") public String state;
    @JsonProperty("conflict") public List<HassioConflictingAttribute> conflicts;

    public HassioConflictState() {
        // Default constructor
    }

    public HassioConflictState(String entityID, String type, Date datetime) {
        this.entity_id = entityID;
        this.type = type;
        this.datetime = datetime;
        this.conflicts = new ArrayList<HassioConflictingAttribute>();
    }
}
