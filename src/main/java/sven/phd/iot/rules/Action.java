package sven.phd.iot.rules;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;
import sven.phd.iot.hassio.states.HassioState;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

abstract public class Action {
    private static int random = 0;

    @JsonProperty("id") public String id;
    @JsonProperty("description") public String description;
    @JsonProperty("enabled") public Boolean enabled;
    @JsonProperty("action_name") public String actionName;

    /* Mathias adding action disabling properties
     *  It should be possible to have multiple start and stop times, so a list should be kept
     */
    @JsonProperty("start_time_disable") public List<Date> startingTimesDisable;

    @JsonProperty("stop_time_disable") public List<Date> stoppingTimesDisable;


    public Action(String description) {
        this.id = ("actionId" + random++);
        this.description = description;
        this.startingTimesDisable = new ArrayList<>();
        this.stoppingTimesDisable = new ArrayList<>();
        this.enabled = true;
        this.actionName = this.getClass().getName();
    }

  //  abstract public void previewHandler(Map<String, HassioState> newState);
   // abstract public void eventHandler(Map<String, HassioState> newState);

    abstract public List<HassioState> simulate(Date datetime, HashMap<String, HassioState> hassioStates);

    /*MATHIAS*/
    public String getActionID() { return id; }

    /*MATHIAS*/
    public String getDeviceID() { return ""; };

    /*MATHIAS*/
    public boolean onSameDevice(Action other) { return id.equals(other.id); }

    /*MATHIAS*/
    public boolean isEnabled(Date currentTime) {
        boolean enabled = this.enabled;

        for (int i = 0; i < stoppingTimesDisable.size(); i++) {
            Date startTime = startingTimesDisable.get(i);
            Date stopTime = stoppingTimesDisable.get(i);
            if (currentTime.compareTo(startTime) >= 0 && currentTime.compareTo(stopTime) <= 0) {
                enabled = false;
            }
        }
        return enabled;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
