package sven.phd.iot.students.mathias.states;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConflictSolution {
    @JsonProperty("entity_id") public String entity_id;
    @JsonProperty("conflicting_actions") public List<ConflictingAction> conflictingActions; // The actions that cause the conflict, used for detecting when the solution should be applied
    @JsonProperty("snoozed_actions") public List<ConflictingAction> snoozedActions;
    @JsonProperty("active_actions") public List<ConflictingAction> activeActions;
    @JsonProperty("custom_action") public Action customAction = null; // Optional

    // For deserialization
    public ConflictSolution(){
        this.entity_id = null;
        this.conflictingActions = new ArrayList<>();
        this.snoozedActions = new ArrayList<>();
        this.activeActions = new ArrayList<>();
        this.customAction = null;
    }

    public ConflictSolution(String entityID){
        this.entity_id = entityID;
        this.conflictingActions = new ArrayList<>();
        this.snoozedActions = new ArrayList<>();
        this.activeActions = new ArrayList<>();
        this.customAction = null;
    }

    public void addConflictingAction(ConflictingAction action) {
        this.conflictingActions.add(action);
    }

    public void snoozeAction(ConflictingAction action, boolean snoozed) {
        if(snoozed) {
            this.snoozedActions.add(action);
        } else {
            this.activeActions.add(action);
        }
    }

    public boolean isSnoozed(ConflictingAction action) {
        return this.snoozedActions.contains(action);
    }

    public boolean matchesConflict(Conflict conflict) {
        if(conflict.conflictingActions.size() != this.conflictingActions.size()) return false;

        // Check if all actions in the conflict match our conflicting actions
        for(ConflictingAction conflictingAction : conflict.conflictingActions) {
            if(!this.conflictingActions.contains(conflictingAction)) return false;
        }

        return true;
    }
}
