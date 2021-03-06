package sven.phd.iot.rules.triggers;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.models.Event;
import sven.phd.iot.rules.Trigger;

import java.util.HashMap;
import java.util.List;

public class EventTrigger extends Trigger {
    public EventTrigger(String id, String title) {
        super(id, title);
    }

    @Override
    public boolean isTriggeredBy(HashMap<String, HassioState> hassioStates, HassioChange hassioChange) {
        return false;
    }

    @Override
    public List<HassioContext> verifyCondition(HashMap<String, HassioState> hassioStates) {
        return null;
    }
   /* private String identifier;
    private Event.ACTION action;

    public EventTrigger(String ruleIdentifier, String identifier, Event.ACTION action) {
        super(ruleIdentifier, "When " + identifier + "[" + action + "]");
        this.identifier = identifier;
        this.action = action;
       // super("button.connectedButton", Event.ACTION.PRESSED);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Event.ACTION getAction() {
        return action;
    }

    public void setAction(Event.ACTION action) {
        this.action = action;
    }

    public void subscribeToContextChanges() {
        System.err.println("TODO: Implement ValueChanged() in EventTrigger");
    }

    public boolean verifyCondition(HassioChange hassioChange) {
        return false;
    }

    @Override
    public boolean isTriggeredBy(HashMap<String, HassioState> hassioStates, HassioChange hassioChange) {
        return true;
    }

    @Override
    public List<HassioContext> verifyCondition(HashMap<String, HassioState> hassioStates) {
        return null;
    } */

}