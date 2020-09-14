package sven.phd.iot.conflicts;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.Future;

import java.util.*;

public class InconsistencyVerifier extends ConflictVerifier {
    /* Als een device meerdere keren in dezelfde layer wordt toegevoegd, wordt dit ook meerdere uitgevoerd */
    @Override
    public List<Conflict> verifyConflicts(Date simulationTime, Future future, HassioState newstate) {
        List<Conflict> conflicts = new ArrayList<>();

        Stack<HassioState> entityStateStack = future.getEntityStateStack(newstate.entity_id);

        Conflict conflict = new Conflict("Inconsistency", simulationTime);
        conflict.addConflictState(newstate);

        for(int i = entityStateStack.size() - 1; i >= 0; --i) {
            HassioState state = entityStateStack.get(i);

            //TODO: Take into account time window
            if(newstate.getLastChanged().equals(state.getLastChanged()) && !newstate.state.equals(state.state)) {
                conflict.addConflictState(state);
            }
        }

        // If more than one state is found, we have a conflict
        if(conflict.getConflictingStates().size() > 1) {
            conflicts.add(conflict);
        }

        return conflicts;
    }
}