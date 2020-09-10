package sven.phd.iot.students.mathias.states;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Conflict {
    @JsonProperty("conflict_type") public String conflictType;
    @JsonProperty("conflicting_entities") public List<String> conflictingEntities;
    @JsonProperty("conflicting_actions") public List<ConflictingAction> conflictingActions;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("conflict_time") protected Date conflictTime;

    public Conflict() {
        // Default constructor
    }

    public Conflict(String conflictType, Date date, List<String> conflictingEntities, List<HassioState> conflictingChanges) {
        this.conflictType = conflictType;
        this.conflictTime = date;
        this.conflictingEntities = conflictingEntities;
        this.conflictingActions = new ArrayList<>();

        for (HassioState node : conflictingChanges) {
            if (node.getExecutionEvent() == null) {
                System.err.println("The conflicting state did not have an execution event for " + node.entity_id + " = " + node.state);
                continue;
            }

            // TODO: FIX
            /*String causingAction = node.getExecutionEvent().getResponsibleAction(node.context);

            if (causingAction == null) {
                // The state is not caused by a rule
                System.err.println("The conflicting state is not caused by a rule");
            }

            this.addAction(new ConflictingAction(causingAction, node.getExecutionEvent().entity_id)); */
        }
    }

    public Conflict(List<String> conflictingEntities, List<HassioState> conflictingChanges) {
        this.conflictingEntities = conflictingEntities;
        this.conflictingActions = new ArrayList<>();

        for (HassioState node : conflictingChanges) {
            if (node.getExecutionEvent() == null) {
                System.err.println("The conflicting state did not have an execution event for " + node.entity_id + " = " + node.state);
                continue;
            }

            // TODO: FIX
            /*String causingAction = node.getExecutionEvent().getResponsibleAction(node.context);

            if (causingAction == null) {
                // The state is not caused by a rule
                System.err.println("The conflicting state is not caused by a rule");
            }
this.addAction(new ConflictingAction(causingAction, node.getExecutionEvent().entity_id)); */
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

    public boolean updateConflict(List<HassioState> conflictingChanges) {
        boolean actionAdded = false;
        for (HassioState node : conflictingChanges) {
            if (node.getExecutionEvent() == null) {
                System.err.println("The conflicting state did not have an execution event for " + node.entity_id + " = " + node.state);
                continue;
            }

            // TODO: FIX
            /* String causingAction = node.getExecutionEvent().getResponsibleAction(node.context);

            if (causingAction == null) {
                // The state is not caused by a rule
                System.err.println("The conflicting state is not caused by a rule");
            }

            ConflictingAction action = new ConflictingAction(causingAction, node.getExecutionEvent().entity_id);
            // Check if action isn't already part of conflict
            if (!this.conflictingActions.contains(action)) {
                this.addAction(action);
                actionAdded = true;
            } */
        }
        return actionAdded;
    }

    public boolean containsSameActions(List<HassioState> conflictingChanges) {
        for (HassioState conflictChange : conflictingChanges) {
            for (ConflictingAction conflictingAction : conflictingActions) {
                // TODO FIX
              //  if (conflictChange.getExecutionEvent().actionContexts.containsKey(conflictingAction.action_id)) {
                    return true;
              //  }
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
