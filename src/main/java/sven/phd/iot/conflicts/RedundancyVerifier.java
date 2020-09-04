package sven.phd.iot.conflicts;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.CausalStack;
import sven.phd.iot.students.mathias.states.Conflict;

import java.util.HashMap;
import java.util.List;

public class RedundancyVerifier extends ConflictVerifier {
    @Override
    public boolean isTriggeredBy(HassioChange hassioChange) {
        return false;
    }

    @Override
    public List<Conflict> verifyConflicts(HashMap<String, HassioState> hassioStates, CausalStack causalStack) {
        return null;
    }
}
