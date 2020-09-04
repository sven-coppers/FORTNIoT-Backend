package sven.phd.iot.conflicts;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.CausalStack;
import sven.phd.iot.students.mathias.states.Conflict;

import java.util.HashMap;
import java.util.List;

// A heater and a cooler in the same room may never be turned on at the same
public class TemperatureConflictVerifier extends ConflictVerifier {
    private String tempSensorID;
    private List<String> coolerIDs;
    private List<String> heaterIDs;

    public TemperatureConflictVerifier(String tempSensorID) {

    }

    @Override
    public boolean isTriggeredBy(HassioChange hassioChange) {
        return false;
    }

    @Override
    public List<Conflict> verifyConflicts(HashMap<String, HassioState> hassioStates, CausalStack causalStack) {
        return null;
    }
}