package sven.phd.iot.students.mathias.states;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.ContextManager;
import sven.phd.iot.conflicts.Conflict;
import sven.phd.iot.overrides.SnoozedAction;
import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.List;

public class ConflictSolution {
    @JsonProperty("entity_id") public String entity_id;
    @JsonProperty("conflicting_actions") public List<SnoozedAction> snoozedActions; // The actions that cause the conflict, used for detecting when the solution should be applied
    @JsonProperty("active_actions") public List<SnoozedAction> activeActions;
    @JsonProperty("custom_actions") public List<Action> customActions; // Optional
    @JsonProperty("solution_id") public String solutionID;

    // For deserialization
    public ConflictSolution(){
        this.entity_id = null;
        this.snoozedActions = new ArrayList<>();
        this.snoozedActions = new ArrayList<>();
        this.activeActions = new ArrayList<>();
        this.customActions = new ArrayList<>();
        this.updateSolutionID();
    }

    public ConflictSolution(String entityID){
        this.entity_id = entityID;
        this.snoozedActions = new ArrayList<>();
        this.snoozedActions = new ArrayList<>();
        this.activeActions = new ArrayList<>();
        this.customActions = new ArrayList<>();
        this.updateSolutionID();
    }

    private void updateSolutionID() {
        this.solutionID = this.entity_id;

        for(SnoozedAction action : this.snoozedActions) {
            this.solutionID += "_" + action.actionID;
        }
    }

    public void setConflictingActions(List<SnoozedAction> snoozedActions) {
        this.snoozedActions = snoozedActions;
        this.updateSolutionID();
    }

    public void addConflictingAction(SnoozedAction action) {
        this.snoozedActions.add(action);
        this.updateSolutionID();
    }

    public void addSolvingAction(Action action) {
        this.customActions.add(action);
    }

    public void snoozeAction(SnoozedAction action, boolean snoozed) {
        if(snoozed) {
            this.snoozedActions.add(action);
        } else {
            this.activeActions.add(action);
        }
    }

    public void setSnoozedActions(List<SnoozedAction> snoozedActions) {
        this.snoozedActions = snoozedActions;
    }

    public void setActiveActions(List<SnoozedAction> activeActions) {
        this.activeActions = activeActions;
    }

    public void setCustomActions(List<Action> customActions) {
        this.customActions = customActions;
    }

    public List<SnoozedAction> getConflictingActions() {
        return this.snoozedActions;
    }

    public List<SnoozedAction> getSnoozedActions() {
        return snoozedActions;
    }

    public List<SnoozedAction> getActiveActions() {
        return activeActions;
    }

    public boolean isSnoozed(SnoozedAction action) {
        return this.snoozedActions.contains(action);
    }

    public boolean matchesConflict(Conflict conflict) {
        // TODO: Fix
      /*  if(conflict.conflictingActions.size() != this.conflictingActions.size()) return false;

        // Check if all actions in the conflict match our conflicting actions
        for(ConflictingAction conflictingAction : conflict.conflictingActions) {
            if(!this.conflictingActions.contains(conflictingAction)) return false;
        } */

        return true;
    }

    public List<Action> getCustomActions() {
        return this.customActions;
    }

    @JsonIgnore
    public boolean isRedundancySolution() {
     /*   Action comparingAction = ContextManager.getInstance().getActionById(snoozedActions.get(0).actionID);
        for (SnoozedAction snoozedAction : this.snoozedActions) {
            Action action = ContextManager.getInstance().getActionById(snoozedAction.actionID);
            if (!action.equals(comparingAction) && !action.isSimilar(comparingAction)) {
                return false;
            }
        } */
        return true;
    }
}
