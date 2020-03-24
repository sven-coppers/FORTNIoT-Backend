package sven.phd.iot.hassio.states;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HassioConflictState {
    @JsonProperty("entity_id") public String entity_id;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("datetime") public Date datetime;

    @JsonProperty("rules") public List<HassioConflictingRuleState> rules;

    public HassioConflictState() {
        // Default constructor
    }

    public HassioConflictState(String entityID, Date datetime) {
        this.entity_id = entityID;
        this.datetime = datetime;
        this.rules = new ArrayList<HassioConflictingRuleState>();
    }

    public boolean alreadyExist(String entityID, Date datetime) {
        return (this.entity_id.equals(entityID) && this.datetime == datetime);
    }

    public boolean containsRule(String ruleId) {
        for (HassioConflictingRuleState rule: rules) {
            if (rule.rule_id.equals(ruleId)) {
                return true;
            }
        }
        return false;
    }
}
