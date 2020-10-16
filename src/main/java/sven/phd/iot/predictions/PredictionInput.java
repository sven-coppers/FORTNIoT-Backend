package sven.phd.iot.predictions;

import sven.phd.iot.api.request.SimulationRequest;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.overrides.SnoozedAction;
import sven.phd.iot.rules.Action;
import sven.phd.iot.rules.Trigger;

import java.util.*;

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
        // Add snoozed actions and custom states from API to what we already have
        this.snoozedActions.addAll(simulationRequest.snoozedActions);


        // Subtract unsnoozed actions and custom states from API from what we already have
        for(String reEnabledAction : simulationRequest.reEnabledActions) {
            Iterator<SnoozedAction> itr = this.snoozedActions.iterator();

            while (itr.hasNext()) {
                SnoozedAction snoozedAction = itr.next();

                if (snoozedAction.equals(reEnabledAction)) {
                    this.snoozedActions.remove(snoozedAction);
                }
            }
        }


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

    /**
     * Return the snoozedAction that snoozes this item, if any, null otherwise
     * @param actionID
     * @param triggerEntityID
     * @param executionTime
     * @return
     */
    public SnoozedAction isSnoozed(String actionID, String triggerEntityID, Date executionTime) {
        for(SnoozedAction snoozedAction : this.snoozedActions) {
            if(snoozedAction.matches(actionID, triggerEntityID, executionTime)) return snoozedAction;
        }

        return null;
    }
}
