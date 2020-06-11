package sven.phd.iot.students.mathias.states;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Conflict {
    @JsonProperty("entity_id") public String entity_id;

    //@JsonDeserialize(using = HassioDateDeserializer.class)
   // @JsonSerialize(using = HassioDateSerializer.class)
   // @JsonProperty("datetime") public Date datetime;

    @JsonProperty("conflicting_actions") public List<ConflictingAction> conflictingActions;

    public Conflict() {
        // Default constructor
    }

    public Conflict(String entityID) {
        this.entity_id = entityID;
        //this.datetime = datetime;
        this.conflictingActions = new ArrayList<ConflictingAction>();
    }

    public void addAction(ConflictingAction conflictingActionState) {
        this.conflictingActions.add(conflictingActionState);
    }

    public boolean alreadyExist(String entityID) {
        return (this.entity_id.equals(entityID));
    }

    public boolean containsRule(String ruleID) {
        for (ConflictingAction action: conflictingActions) {
            if (action.rule_id.equals(ruleID)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAction(String actionID) {
        for (ConflictingAction action: conflictingActions) {
            if (action.action_id.equals(actionID)) {
                return true;
            }
        }
        return false;
    }
}
