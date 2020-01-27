package sven.phd.iot.students.bram.questions.why.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HassioUser {
    @JsonProperty("id")
    public String id;
    @JsonProperty("name")
    public String name;
    @JsonProperty("is_owner")
    public boolean is_owner;
    @JsonProperty("is_active")
    public boolean is_active;
    @JsonProperty("system_generated")
    public boolean system_generated;

}
