package sven.phd.iot.students.mathias.states;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;

import java.util.Date;

public class SnoozedAction {
    @JsonProperty("action_id") public String actionID;
    @JsonProperty("conflict_time_window") public long window;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("conflict_time") public Date conflictTime;

    // For deserialization
    public SnoozedAction(){

    }

    public SnoozedAction(String actionID, Date datetime, long window) {
        this.actionID = actionID;
        this.window = window;
        this.conflictTime = datetime;
    }
}
