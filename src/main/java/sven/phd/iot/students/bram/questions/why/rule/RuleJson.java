package sven.phd.iot.students.bram.questions.why.rule;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RuleJson {
    @JsonProperty("trigger")
    public String trigger;
    @JsonProperty("action")
    public String action;
    @JsonProperty("rule_id")
    public String rule_id;
}
