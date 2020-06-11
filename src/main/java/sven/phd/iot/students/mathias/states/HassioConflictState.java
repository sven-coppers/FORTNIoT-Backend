package sven.phd.iot.students.mathias.states;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HassioConflictState {
    @JsonProperty("entity_id") public String entity_id;

    //@JsonDeserialize(using = HassioDateDeserializer.class)
   // @JsonSerialize(using = HassioDateSerializer.class)
   // @JsonProperty("datetime") public Date datetime;

    @JsonProperty("actions") public List<HassioConflictingActionState> actions;

    public HassioConflictState() {
        // Default constructor
    }

    public HassioConflictState(String entityID) {
        this.entity_id = entityID;
        //this.datetime = datetime;
        this.actions = new ArrayList<HassioConflictingActionState>();
    }

    public void addAction(HassioConflictingActionState conflictingActionState) {
        this.actions.add(conflictingActionState);
    }

    public boolean alreadyExist(String entityID) {
        return (this.entity_id.equals(entityID));
    }

    public boolean containsRule(String ruleID) {
        for (HassioConflictingActionState action: actions) {
            if (action.rule_id.equals(ruleID)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAction(String actionID) {
        for (HassioConflictingActionState action: actions) {
            if (action.action_id.equals(actionID)) {
                return true;
            }
        }
        return false;
    }
}
