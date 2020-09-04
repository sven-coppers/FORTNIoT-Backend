package sven.phd.iot.students.mathias.states;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.ContextManager;
import sven.phd.iot.predictions.CausalNode;
import sven.phd.iot.rules.Action;

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

            this.addAction(new ConflictingAction(causingAction, node.getExecutionEvent().entity_id));
        }
    }

    public void addAction(ConflictingAction conflictingActionState) {
        this.conflictingActions.add(conflictingActionState);
    }

    public List<ConflictingAction> getConflictingActions() {
        return conflictingActions;
    }

    public void setConflictingActions(List<ConflictingAction> conflictingActions) {
        this.conflictingActions = conflictingActions;
    }

    public boolean updateConflict(List<CausalNode> conflictingChanges) {
        boolean actionAdded = false;
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

            ConflictingAction action = new ConflictingAction(causingAction, node.getExecutionEvent().entity_id);
            // Check if action isn't already part of conflict
            if (!this.conflictingActions.contains(action)) {
                this.addAction(action);
                actionAdded = true;
            }
        }
        return actionAdded;
    }

    public boolean containsSameActions(List<CausalNode> conflictingChanges) {
        for (CausalNode conflictChange : conflictingChanges) {
            for (ConflictingAction conflictingAction : conflictingActions) {
                if (conflictChange.getExecutionEvent().actionContexts.containsKey(conflictingAction.action_id)) {
                    return true;
                }
            }
        }
        return false;
    }

    @JsonIgnore
    public boolean isLoop() {
        List<String> devices = new ArrayList<>();
        for (ConflictingAction conflictAction : conflictingActions) {
            Action actionType = ContextManager.getInstance().getActionById(conflictAction.action_id);

            if(actionType != null) {
                String deviceID = actionType.getDeviceID();
                if (devices.isEmpty()) {
                    devices.add(deviceID);
                } else if (!devices.contains(deviceID)) {
                    return true;
                }
            }
        }
        return false;
    }

    @JsonIgnore
    public boolean isRedundancy() {
        for (int i = 0, j = 1; j < conflictingActions.size(); ++i, ++j) {
            if (!ContextManager.getInstance().getActionById(conflictingActions.get(i).action_id).isSimilar(ContextManager.getInstance().getActionById(conflictingActions.get(j).action_id))) {
                return false;
            }
        }
        return true;
    }
}
