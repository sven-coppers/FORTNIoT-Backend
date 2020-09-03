package sven.phd.iot.conflicts;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioState;

import java.util.HashMap;
import java.util.List;

public class RedundancyVerifier extends ConflictVerifier {
    @Override
    public boolean isTriggeredBy(HassioChange hassioChange) {
        return false;
    }

    @Override
    public List<HassioState> verifyCondition(HashMap<String, HassioState> hassioStates) {
        return null;
    }
}
