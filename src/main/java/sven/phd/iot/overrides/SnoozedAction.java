package sven.phd.iot.overrides;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;

import java.util.Date;

public class SnoozedAction {
    private static int uniqueCounter = 0;

    @JsonProperty("action_id") public String actionID;
    @JsonProperty("conflict_time_window") public long window; // in seconds
    @JsonProperty("trigger_entity_id") public String triggerEntityID;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)

    @JsonProperty("conflict_time") public Date conflictTime;
    @JsonProperty("snoozed_action_id") public String snoozedActionID; // Can only be used for DELETING the right snoozed action

    // For deserialization
    public SnoozedAction(){
        this.snoozedActionID = ("snoozedActionId" + uniqueCounter++);
    }

    public SnoozedAction(String actionID, Date datetime, long window, String triggerEntityID) {
        this.snoozedActionID = ("snoozedActionId" + uniqueCounter++);
        this.actionID = actionID;
        this.window = window;
        this.conflictTime = datetime;
        this.triggerEntityID = triggerEntityID;
    }

    public boolean matches(String otherActionID, String otherTriggerEntityID, Date executionTime) {
        if(!this.actionID.equals(otherActionID)) return false;
        if(!this.triggerEntityID.equals(otherTriggerEntityID)) return false;

        long windowInMilliSeconds = this.window * 1000;

        if(executionTime.getTime() < this.conflictTime.getTime() - windowInMilliSeconds) return false;
        if(executionTime.getTime() > this.conflictTime.getTime() + windowInMilliSeconds) return false;

        return true;
    }
}
