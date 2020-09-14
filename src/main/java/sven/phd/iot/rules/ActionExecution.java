package sven.phd.iot.rules;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActionExecution {
    private static int identifier = 0; // Increased every time it is used

    @JsonProperty("action_execution_id") private String actionExecutionID;
    @JsonProperty("action_id") private String actionID;
    @JsonProperty("resulting_contexts") public List<HassioContext> resultingContexts;

    public ActionExecution() {
        // Default constructor for deserialization
    }

    public ActionExecution(String actionID, HassioContext resultingContext) {
        this(actionID, new ArrayList<>());
        this.resultingContexts.add(resultingContext);
    }

    public ActionExecution(String actionID, List<HassioState> resultingStates) {
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