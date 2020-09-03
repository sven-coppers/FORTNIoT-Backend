package sven.phd.iot.conflicts;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioState;

import java.util.HashMap;
import java.util.List;

abstract public class ConflictVerifier {
    private boolean enabled;

    // Implementation similar to trigger / condition

    /**
     * Check if the rule is interested in being verified after this change (e.g. temp update)
     * @param hassioChange the change that this rule might be interested in
     * @return true if the rule is triggered by this changed, false otherwise.
     */

    public abstract boolean isTriggeredBy(HassioChange hassioChange);

    /**
     * Check if the hassioChange causes this trigger to be triggered
     * @param hassioStates a map with states for each device
     * @return a list of HassioContexts that satisfy the condition of the rule, returns null when the rule it NOT triggered, returns an empty list when the rule is satisfied without any states
     */
    public abstract List<HassioState> verifyCondition(HashMap<String, HassioState> hassioStates);

    public ConflictVerifier() {
        this.enabled = true;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
