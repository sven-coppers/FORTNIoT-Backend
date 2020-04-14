package sven.phd.iot.students.mathias.states;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;

import java.util.Date;

public class HassioConflictingRuleState {
    @JsonProperty("rule_id") public String rule_id;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("triggered_time") public Date triggered_time;

    public HassioConflictingRuleState(String rule_id, Date triggered_time) {
        this.rule_id = rule_id;
        this.triggered_time = triggered_time;
    }
}
