package sven.phd.iot.conflicts;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.Future;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

public class NoEffectVerifier extends ConflictVerifier {

    @Override
    public List<Conflict> verifyConflicts(Date simulationTime, Future future, HassioState newstate) {
        List<Conflict> conflicts = new ArrayList<>();

        Stack<HassioState> entityStateStack = future.getEntityStateStack(newstate.entity_id);

        Conflict conflict = new Conflict(this, simulationTime);

        if(entityStateStack.size() > 0){
            HassioState lastState = entityStateStack.get(entityStateStack.size() - 1);

            if(newstate.state.equals(lastState.state)) {
                conflict.addConflictState(newstate);
            //    conflict.addConflictState(lastState);
                conflicts.add(conflict);
            }
        }

        return conflicts;
    }

    @Override
    public String getConflictType() {
        return "No effect";
    }
}