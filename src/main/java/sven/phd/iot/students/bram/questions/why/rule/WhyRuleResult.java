package sven.phd.iot.students.bram.questions.why.rule;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;
import sven.phd.iot.students.bram.questions.why.WhyResult;

public class WhyRuleResult extends WhyResult {
    @JsonProperty("rule_id")
    public String rule_id;
    @JsonProperty("rule")
    public RuleJson rule;
}
