package sven.phd.iot.hassio.states;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.UUID;

public class HassioContext {
    private static int random = 0;

    @JsonProperty("id") public String id;
    @JsonProperty("parent_id") public String parent_id;
    @JsonProperty("user_id") public String user_id;

    /** ONLY to be used for deserialisation */
    public HassioContext() {
        this.id = null;
        this.parent_id = null;
        this.user_id = null;
    }

    public HassioContext(String entity_id, Date date) {
        this.id = ("id" + random++);

     //   this.id = "" + (entity_id + date.toString()).hashCode();
        this.parent_id = null;
        this.user_id = null;
    }
}