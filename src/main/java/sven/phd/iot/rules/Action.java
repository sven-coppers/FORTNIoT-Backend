package sven.phd.iot.rules;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;

import java.util.List;

abstract public class Action {
    @JsonProperty("description") public String description;

    public Action(String description) {
        this.description = description;
    }

  //  abstract public void previewHandler(Map<String, HassioState> newState);
   // abstract public void eventHandler(Map<String, HassioState> newState);

    abstract public List<HassioState> simulate(HassioRuleExecutionEvent hassioRuleExecutionEvent);

    @Override
    public String toString() {
        return this.description;
    }
}
