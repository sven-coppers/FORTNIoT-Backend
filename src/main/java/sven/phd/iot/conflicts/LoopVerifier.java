package sven.phd.iot.conflicts;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.Future;
import sven.phd.iot.students.mathias.states.Conflict;

import java.util.Date;
import java.util.List;

public class LoopVerifier extends ConflictVerifier {

    @Override
    public List<Conflict> verifyConflicts(Date simulationTime, Future future, HassioState hassioState) {
        return null;
    }
}
