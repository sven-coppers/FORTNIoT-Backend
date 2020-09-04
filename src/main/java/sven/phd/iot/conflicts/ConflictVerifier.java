package sven.phd.iot.conflicts;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.CausalStack;
import sven.phd.iot.students.mathias.states.Conflict;

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
     * @param previousStates a map with states for each device
     * @param causalStack the causalStack of the current tick
     * @return a list Conflicts that may be caused
     */
    public abstract List<Conflict> verifyConflicts(HashMap<String, HassioState> previousStates, CausalStack causalStack);

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
