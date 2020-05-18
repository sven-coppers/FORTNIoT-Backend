package sven.phd.iot.students.mathias.states;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.states.HassioDateSerializer;
import sven.phd.iot.students.mathias.request.SolutionRequest;

import java.util.Date;

public class HassioConflictSolutionActionState {
    @JsonProperty("entity_id") public String entity_id;
    @JsonProperty("type") public String type;
    @JsonProperty("values") public SolutionRequest values;

    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("action_time") public Date action_time;


    public HassioConflictSolutionActionState(){}

    public HassioConflictSolutionActionState(String actionID, String actionType, Date datetime, SolutionRequest values){
        this.entity_id = actionID;
        this.type = actionType;
        this.action_time = datetime;
        this.values = values;
    }

    public HassioConflictSolutionActionState(HassioConflictSolutionActionState other) {
        this.entity_id = other.entity_id;
        this.type = other.type;
        this.action_time = other.action_time;
        this.values = other.values;
    }
}
