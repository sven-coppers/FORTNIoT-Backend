package sven.phd.iot.students.bram.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
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
