package sven.phd.iot.students.mathias.states;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.json.JSONArray;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;
import sven.phd.iot.rules.Action;
import sven.phd.iot.students.mathias.request.SolutionRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HassioConflictSolutionActionState {
    @JsonProperty("action_id") public String action_id;
    @JsonProperty("description") public String title;
    @JsonProperty("type") public String type;
    @JsonProperty("values") public SolutionRequest values;

    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("action_time") public Date action_time;


    public HassioConflictSolutionActionState(){}

    public HassioConflictSolutionActionState(String actionID, String description, String actionType, Date datetime, SolutionRequest values){
        this.action_id = actionID;
        this.title = description;
        this.type = actionType;
        this.action_time = datetime;
        this.values = values;
    }

    public HassioConflictSolutionActionState(HassioConflictSolutionActionState other) {
        this.action_id = other.action_id;
        this.title = other.title;
        this.type = other.type;
        this.action_time = other.action_time;
        this.values = other.values;
    }
}
