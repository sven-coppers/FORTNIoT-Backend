package sven.phd.iot.rules;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

abstract public class Action {
    private static int random = 0;

    @JsonProperty("id") public String id;
    @JsonProperty("description") public String description;

    /* Mathias adding action disabling properties
     *  It should be possible to have multiple start and stop times, so a list should be kept
     */
    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("start_time_disable") public Date startTimeDisable;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("stop_time_disable") public Date stopTimeDisable;


    public Action(String description) {
        this.id = ("actionId" + random++);
        this.description = description;
        this.startTimeDisable = null;
        this.stopTimeDisable = null;
    }

  //  abstract public void previewHandler(Map<String, HassioState> newState);
   // abstract public void eventHandler(Map<String, HassioState> newState);

    abstract public List<HassioState> simulate(HassioRuleExecutionEvent hassioRuleExecutionEvent, HashMap<String, HassioState> hassioStates);

    /*MATHIAS*/
    public String getActionID() { return id; }

    /*MATHIAS*/
    public String getDeviceID() { return ""; };

    /*MATHIAS*/
    public boolean onSameDevice(Action other) { return id.equals(other.id); }

    /*MATHIAS*/
    public boolean isEnabled(Date currentTime) {
        boolean enabled = true;

        if (startTimeDisable != null && currentTime.compareTo(startTimeDisable) >= 0) {
            enabled = false;
        }
        if (stopTimeDisable != null && currentTime.compareTo(stopTimeDisable) > 0) {
            enabled = true;
        }
        return enabled;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
