package sven.phd.iot.hassio.states;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class HassioContext {
    @JsonProperty("id") public String id;
    @JsonProperty("parent_id") public String parent_id;
    @JsonProperty("user_id") public String user_id;

    public HassioContext() {
        this.id = UUID.randomUUID().toString();
        this.parent_id = null;
        this.user_id = null;
    }

    public HassioContext(String id) {
        this.id = id;
        this.parent_id = null;
        this.user_id = null;
    }
}