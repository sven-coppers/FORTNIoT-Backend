package sven.phd.iot.students.bram.questions.why;


import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.students.bram.user.User;

public class WhyUserResult extends WhyResult {
    @JsonProperty("user_id")
    public String user_id;
    @JsonProperty("user")
    public User user;
}
