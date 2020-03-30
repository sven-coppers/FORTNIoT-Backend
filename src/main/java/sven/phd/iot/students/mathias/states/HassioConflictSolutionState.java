package sven.phd.iot.students.mathias.states;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HassioConflictSolutionState {
    @JsonProperty("entity_id") public String entity_id;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("datetime") public Date datetime;

    @JsonProperty("actions") public List<HassioConflictSolutionActionState> actions;

    public HassioConflictSolutionState(String entityID, Date datetime){
        this.entity_id = entityID;
        this.datetime = datetime;
        this.actions = new ArrayList<HassioConflictSolutionActionState>();
    }
}
