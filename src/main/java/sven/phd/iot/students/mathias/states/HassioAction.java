package sven.phd.iot.students.mathias.states;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HassioAction {
    @JsonProperty("action_name") public String actionName;
    @JsonProperty("description") public String description;

    public HassioAction(String actionName, String description){
        this.actionName = actionName;
        this.description = description;
    }
}
