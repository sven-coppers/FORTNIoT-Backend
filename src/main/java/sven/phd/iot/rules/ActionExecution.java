package sven.phd.iot.rules;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;
import sven.phd.iot.hassio.states.HassioState;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ActionExecution {
    private static int identifier = 0; // Increased every time it is used

    @JsonProperty("action_execution_id") private String actionExecutionID;
    @JsonProperty("action_id") private String actionID;
    @JsonProperty("resulting_contexts") public List<HassioContext> resultingContexts;


    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("datetime") public Date datetime;

    public ActionExecution() {
        // Default constructor for deserialization
    }

    public ActionExecution(Date datetime, String actionID, HassioContext resultingContext) {
        this(datetime, actionID, new ArrayList<>());
        this.resultingContexts.add(resultingContext);
    }

    public ActionExecution(Date datetime, String actionID, List<HassioState> resultingStates) {
        this.datetime = datetime;
        this.setActionID(actionID);
        this.resultingContexts = new ArrayList<>();
        this.actionExecutionID = "action_execution_id_" + this.identifier++;

        for(HassioState resultingState : resultingStates) {
            this.resultingContexts.add(resultingState.context);
        }
    }

    public String getActionExecutionID() {
        return actionExecutionID;
    }

    public String getActionID() {
        return actionID;
    }

    public void setActionID(String actionID) {
        this.actionID = actionID;
    }
}