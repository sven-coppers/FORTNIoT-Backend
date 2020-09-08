package sven.phd.iot.conflicts;

import sven.phd.iot.predictions.CausalNode;
import sven.phd.iot.predictions.Future;
import sven.phd.iot.students.mathias.states.Conflict;

import java.util.Date;
import java.util.List;

public class RedundancyVerifier extends ConflictVerifier {

    @Override
    public List<Conflict> verifyConflicts(Date simulationTime, Future future, CausalNode newCausalNode) {
        return null;
    }
}
