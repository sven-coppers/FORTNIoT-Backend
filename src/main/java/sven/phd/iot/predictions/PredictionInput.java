package sven.phd.iot.predictions;

import sven.phd.iot.api.request.SimulationRequest;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.overrides.SnoozedAction;
import sven.phd.iot.rules.Action;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PredictionInput {
    private List<HassioState> hassioStates;
    private HashMap<String, Boolean> enabledRules;
    private List<SnoozedAction> snoozedActions;

    public PredictionInput() {
        this.setHassioStates(new ArrayList<>());
        this.setEnabledRules(new HashMap<>());
        this.setSnoozedActions(new ArrayList<>());
    }

    public void mergeSimulationRequest(SimulationRequest simulationRequest) {
        // TODO: Add snoozed actions and custom states from API to what we already have
        this.snoozedActions.addAll(simulationRequest.snoozedActions);


        // TODO: subtract unsnoozed actions and custom states from API from what we already have

        // Remove an action from the list of snoozed actions when it should be reenabled
      /*  for(SnoozedAction snoozedAction : this.snoozedActions) {
            if(snoozedAction.m)
        }

        for(String reEnabledActionExecutionID : simulationRequest.reEnabledActions) {

        }*/
    }

    public List<HassioState> getHassioStates() {
        return hassioStates;
    }

    public void setHassioStates(List<HassioState> hassioStates) {
        this.hassioStates = hassioStates;
    }

    public HashMap<String, Boolean> getEnabledRules() {
        return enabledRules;
    }

    public void setEnabledRules(HashMap<String, Boolean> enabledRules) {
        this.enabledRules = enabledRules;
    }

    public List<SnoozedAction> getSnoozedActions() {
        return snoozedActions;
    }

    public void setSnoozedActions(List<SnoozedAction> snoozedActions) {
        this.snoozedActions = snoozedActions;
    }

    public boolean isSnoozed(String actionID, String triggerEntityID, Date executionTime) {
        for(SnoozedAction snoozedAction : this.snoozedActions) {
            if(snoozedAction.matches(actionID, triggerEntityID, executionTime)) return true;
        }

        return false;
    }
}
