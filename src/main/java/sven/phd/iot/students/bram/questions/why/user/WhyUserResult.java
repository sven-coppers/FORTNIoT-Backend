package sven.phd.iot.students.bram.questions.why.user;


import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.students.bram.questions.why.WhyResult;

public class WhyUserResult extends WhyResult {
    @JsonProperty("user_id")
    public String user_id;
    @JsonProperty("user")
    public HassioUser user;
}
