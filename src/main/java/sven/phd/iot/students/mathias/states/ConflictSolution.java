package sven.phd.iot.students.mathias.states;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.ContextManager;
import sven.phd.iot.conflicts.Conflict;
import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.List;

public class ConflictSolution {
    @JsonProperty("entity_id") public String entity_id;
    @JsonProperty("conflicting_actions") public List<ConflictingAction> conflictingActions; // The actions that cause the conflict, used for detecting when the solution should be applied
    @JsonProperty("snoozed_actions") public List<ConflictingAction> snoozedActions;
    @JsonProperty("active_actions") public List<ConflictingAction> activeActions;
    @JsonProperty("custom_actions") public List<Action> customActions; // Optional
    @JsonProperty("solution_id") public String solutionID;

    // For deserialization
    public ConflictSolution(){
        this.entity_id = null;
        this.conflictingActions = new ArrayList<>();
        this.snoozedActions = new ArrayList<>();
        this.activeActions = new ArrayList<>();
        this.customActions = new ArrayList<>();
        this.updateSolutionID();
    }

    public ConflictSolution(String entityID){
        this.entity_id = entityID;
        this.conflictingActions = new ArrayList<>();
        this.snoozedActions = new ArrayList<>();
        this.activeActions = new ArrayList<>();
        this.customActions = new ArrayList<>();
        this.updateSolutionID();
    }

    private void updateSolutionID() {
        this.solutionID = this.entity_id;

        for(ConflictingAction action : this.conflictingActions) {
            this.solutionID += "_" + action.action_id;
        }
    }

    public void setConflictingActions(List<ConflictingAction> conflictingActions) {
        this.conflictingActions = conflictingActions;
        this.updateSolutionID();
    }

    public void addConflictingAction(ConflictingAction action) {
        this.conflictingActions.add(action);
        this.updateSolutionID();
    }

    public void addSolvingAction(Action action) {
        this.customActions.add(action);
    }

    public void snoozeAction(ConflictingAction action, boolean snoozed) {
        if(snoozed) {
            this.snoozedActions.add(action);
        } else {
            this.activeActions.add(action);
        }
    }

    public void setSnoozedActions(List<ConflictingAction> snoozedActions) {
        this.snoozedActions = snoozedActions;
    }

    public void setActiveActions(List<ConflictingAction> activeActions) {
        this.activeActions = activeActions;
    }

    public void setCustomActions(List<Action> customActions) {
        this.customActions = customActions;
    }

    public List<ConflictingAction> getConflictingActions() {
        return this.conflictingActions;
    }

    public List<ConflictingAction> getSnoozedActions() {
        return snoozedActions;
    }

    public List<ConflictingAction> getActiveActions() {
        return activeActions;
    }

    public boolean isSnoozed(ConflictingAction action) {
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
        Action comparingAction = ContextManager.getInstance().getActionById(conflictingActions.get(0).action_id);
        for (ConflictingAction conflictingAction : conflictingActions) {
            Action action = ContextManager.getInstance().getActionById(conflictingAction.action_id);
            if (!action.equals(comparingAction) && !action.isSimilar(comparingAction)) {
                return false;
            }
        }
        return true;
    }
}
