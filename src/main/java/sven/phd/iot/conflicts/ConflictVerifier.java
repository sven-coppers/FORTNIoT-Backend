package sven.phd.iot.conflicts;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.Future;

import java.util.Date;
import java.util.List;

abstract public class ConflictVerifier {
    private boolean enabled;

    // Implementation similar to trigger / condition

    /**
     * Check if the rule is interested in being verified after this change (e.g. temp update)
     *
     * @param hassioState
     * @return true if the rule is triggered by this changed, false otherwise.
     */

    public boolean isInterestedIn(HassioState hassioState) {
        return true;
    }

    /**
     * Check if the hassioChange causes this trigger to be triggered
     *
     * @param simulationTime
     * @param future
     * @param hassioState
     * @return a list Conflicts that may be caused
     */
    public abstract List<Conflict> verifyConflicts(Date simulationTime, Future future, HassioState hassioState);

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
