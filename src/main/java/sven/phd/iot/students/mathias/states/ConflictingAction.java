package sven.phd.iot.students.mathias.states;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;

import java.util.Date;

public class ConflictingAction {
    @JsonProperty("action_id") public String action_id;
    @JsonProperty("rule_id") public String rule_id;

    public ConflictingAction(String action_id, String rule_id) {
        this.action_id = action_id;
        this.rule_id = rule_id;
    }

    @Override
    public boolean equals(Object object) {
        if(object == this) return true;
        if(!(object instanceof ConflictingAction)) return false;

        ConflictingAction otherAction = (ConflictingAction) object;

        return this.action_id.equals(otherAction.action_id) && this.rule_id.equals(otherAction.rule_id);
    }
}
