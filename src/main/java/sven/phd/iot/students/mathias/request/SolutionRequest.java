package sven.phd.iot.students.mathias.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.json.JSONArray;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;
import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SolutionRequest {
    @JsonProperty("rule_id") public String ruleID;
    @JsonProperty("action_id") public String actionID;
    @JsonProperty("description") public String description;

    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("start_time") public Date startTime;

    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("stop_time") public Date stopTime;

    @JsonProperty("attributes") public HashMap<String, String> attributes;

    public SolutionRequest(){}

    public SolutionRequest(String ruleID, String actionID, String description, Date startTime, Date stopTime, ArrayList<Action> actions) {
        this.ruleID = ruleID;
        this.actionID = actionID;
        this.description = description;
        this.startTime = startTime;
        this.stopTime = stopTime;
        //this.actions = actions;
    }

    public SolutionRequest(SolutionRequest other) {
        this.description = other.description;
        this.startTime = other.startTime;
        this.stopTime = other.stopTime;
        this.attributes = other.attributes;
    }
}
