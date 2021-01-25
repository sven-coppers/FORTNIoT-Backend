package sven.phd.iot.conflicts;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.Future;
import sven.phd.iot.rules.RuleExecution;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

public class RedundancyVerifier extends ConflictVerifier {

    @Override
    public List<Conflict> verifyConflicts(Date simulationTime, Future future, HassioState newstate) {
        List<Conflict> conflicts = new ArrayList<>();

        Stack<HassioState> entityStateStack = future.getEntityStateStack(newstate.entity_id);
        HassioDevice device = ContextManager.getInstance().getHassioDeviceManager().getDevice(newstate.entity_id);

        Conflict conflict = new Conflict(this, simulationTime);
        conflict.addConflictState(newstate);

        for(int i = entityStateStack.size() - 1; i >= 0; --i) {
            HassioState pastState = entityStateStack.get(i);

            // If we are too far back in the past
            if(newstate.getLastChanged().getTime() - device.getChangeCoolDown() > pastState.getLastChanged().getTime()) {
                break;
            }

            // If pastState is too recent and changed
            if(newstate.state.equals(pastState.state)) {
                RuleExecution newRuleExecution = future.getExecutionByContext(newstate.context);
                RuleExecution pastRuleExecution = future.getExecutionByContext(pastState.context);

                // Niet herkennen als het geen loop is (loops hebben dezelfde rule die dezelfde trigger heeft
                if(!newRuleExecution.ruleID.equals(pastRuleExecution.ruleID) || !newRuleExecution.triggerContext.id.equals(pastRuleExecution.triggerContext.id)) {
                    conflict.addConflictState(pastState);
                }
            }
        }

        // If more than one state is found, we have a conflict
        if(conflict.getConflictingStates().size() > 1) {
            conflicts.add(conflict);
        }

        return conflicts;
    }

    @Override
    public String getConflictType() {
        return "Redundancy";
    }
}
