package sven.phd.iot.rules;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;

import java.util.List;

abstract public class Action {
    private String title;

    public Action(String title) {
        this.title = title;
    }

  //  abstract public void previewHandler(Map<String, HassioState> newState);
   // abstract public void eventHandler(Map<String, HassioState> newState);

    abstract public List<HassioState> simulate(HassioRuleExecutionEvent hassioRuleExecutionEvent);

    @Override
    public String toString() {
        return this.title;
    }
}
