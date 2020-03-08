package sven.phd.iot.students.bram.questions.why_not;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WhyNotRule {
    @JsonProperty("rule_id")
    public String ruleId;
    @JsonProperty("trigger")
    public String trigger;
    @JsonProperty("current_states")
    public List<CurrentState> currentStates;
    @JsonProperty("is_triggered")
    public Boolean isTriggered;
}

