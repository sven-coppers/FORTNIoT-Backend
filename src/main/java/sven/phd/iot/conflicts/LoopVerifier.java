package sven.phd.iot.conflicts;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.Future;
import sven.phd.iot.rules.RuleExecution;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoopVerifier extends ConflictVerifier {

    @Override
    public List<Conflict> verifyConflicts(Date simulationTime, Future future, HassioState newstate) {
        List<Conflict> conflicts = new ArrayList<>();
        HassioDevice device = ContextManager.getInstance().getHassioDeviceManager().getDevice(newstate.entity_id);

        Conflict conflict = new Conflict("Loop", simulationTime);
        conflict.addConflictState(newstate);
        conflict.setDetrimental(true);

        RuleExecution newRuleExecution = future.getExecutionByContext(newstate.context);

        if(newRuleExecution == null) return conflicts;

        RuleExecution previousRuleExecution = future.getExecutionByContext(newRuleExecution.triggerContext);

        while(previousRuleExecution != null && newRuleExecution.datetime.getTime() - device.getChangeCoolDown() < previousRuleExecution.datetime.getTime()) {
            HassioState previousState = future.getFutureStateByContext(previousRuleExecution.triggerContext, previousRuleExecution.triggerEntity);
            conflict.addConflictState(previousState);

            // If the same rule is executed twice
            if(newRuleExecution.ruleID.equals(previousRuleExecution.ruleID) && newRuleExecution.triggerEntity.equals(previousRuleExecution.triggerEntity)) {
                conflicts.add(conflict);
                break;
            } else {
                previousRuleExecution = future.getExecutionByContext(previousRuleExecution.triggerContext);
            }
        }

        return conflicts;
    }
}
