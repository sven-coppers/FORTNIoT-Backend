package sven.phd.iot.rules.triggers;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is a 'hack' to represent implicit behavior, which cannot be modelled as rules
 */
public class NeverTrigger extends Trigger {
    public NeverTrigger(String id, String title) {
        super(id, title);
        this.enabled = true;
        this.available = false;
    }

    @Override
    public boolean isTriggeredBy(HassioChange hassioChange) {
        return false; // Never
    }

    @Override
    public List<HassioContext> verifyCondition(HashMap<String, HassioState> hassioStates) {
        return null; // Never
    }

    public void setEnabled(boolean enabled) {
        // DO NOTHING
    }

    public void setAvailable(boolean available) {
        // DO NOTHING
    }
    @Override
    public List<String> getTriggeringEntities() {
        List<String> result = new ArrayList<>();
        result.add("never");
        return result;
    }
}
