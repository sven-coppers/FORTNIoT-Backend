package sven.phd.iot.students.bram.questions.why_not;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.students.bram.questions.why.WhyResult;
import sven.phd.iot.students.bram.questions.why.rule.RuleJson;

import java.util.List;

public class WhyNotResult {
    @JsonProperty("why")
    public WhyResult why;
    @JsonProperty("rules")
    public List<WhyNotRule> rules;
}
