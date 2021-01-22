package sven.phd.iot.conflicts;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.Future;

import java.util.*;

public class InconsistencyVerifier extends ConflictVerifier {
    /* Als een device meerdere keren in dezelfde layer wordt toegevoegd, wordt dit ook meerdere uitgevoerd */
    @Override
    public List<Conflict> verifyConflicts(Date simulationTime, Future future, HassioState newstate) {
        List<Conflict> conflicts = new ArrayList<>();

        Stack<HassioState> entityStateStack = future.getEntityStateStack(newstate.entity_id);
        HassioDevice device = ContextManager.getInstance().getHassioDeviceManager().getDevice(newstate.entity_id);

        Conflict conflict = new Conflict("Inconsistency", simulationTime);
        conflict.addConflictState(newstate);

        for(int i = entityStateStack.size() - 1; i >= 0; --i) {
            HassioState pastState = entityStateStack.get(i);

            // If we are too far back in the past
            if(pastState == null || newstate.getLastChanged().getTime() - device.getChangeCoolDown() > pastState.getLastChanged().getTime()) {
                break;
            }

            // If pastState is too recent and changed
            if(!newstate.state.equals(pastState.state)) {
                conflict.addConflictState(pastState);
            }
        }

        // If more than one state is found, we have a conflict
        if(conflict.getConflictingStates().size() > 1) {
            conflicts.add(conflict);
        }

        return conflicts;
    }
}