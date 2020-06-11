package sven.phd.iot.students.mathias.states;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.predictions.CausalNode;

import java.util.ArrayList;
import java.util.List;

public class Conflict {
    @JsonProperty("entity_id") public String entity_id;
    @JsonProperty("conflicting_actions") public List<ConflictingAction> conflictingActions;

    public Conflict() {
        // Default constructor
    }

    public Conflict(String entityID, List<CausalNode> conflictingChanges) {
        this.entity_id = entityID;
        this.conflictingActions = new ArrayList<ConflictingAction>();

        for (CausalNode node : conflictingChanges) {
            if (node.getExecutionEvent() == null) {
                System.err.println("The conflicting state did not have an execution event for " + node.getState().entity_id + " = " + node.getState().state);
                continue;
            }

            String causingAction = node.getExecutionEvent().getResponsibleAction(node.getState().context);

            if (causingAction == null) {
                // The state is not caused by a rule
                System.err.println("The conflicting state is not caused by a rule");
            }

            this.addAction(new ConflictingAction(causingAction, node.getExecutionEvent().getTrigger().id));
        }
    }

    public void addAction(ConflictingAction conflictingActionState) {
        this.conflictingActions.add(conflictingActionState);
    }
}
