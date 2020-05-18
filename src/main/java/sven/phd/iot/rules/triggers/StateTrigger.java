package sven.phd.iot.rules.triggers;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StateTrigger extends Trigger {
    private String entityID;
    private String triggerState;

    public StateTrigger(String ruleIdentifier, String entityID, String triggerState, String triggerDescription) {
        super(ruleIdentifier, triggerDescription);

        this.entityID = entityID;
        this.triggerState = triggerState;
    }

    @Override
    public boolean isTriggeredBy(HassioChange hassioChange) {
        if(hassioChange.entity_id.equals(entityID)) {
            String oldState = hassioChange.hassioChangeData.oldState.state;
            String newState = hassioChange.hassioChangeData.newState.state;

            // If the state
            return !oldState.equals(newState);
        }

        return false;
    }

    @Override
    public List<HassioContext> verifyCondition(HashMap<String, HassioState> hassioStateHashMap) {
        HassioState hassioAbstractState = hassioStateHashMap.get(entityID);

        if(hassioAbstractState == null) return null;

        if(hassioAbstractState.state.equals(triggerState)) {
            List<HassioContext> triggerContexts = new ArrayList<>();
            triggerContexts.add(hassioAbstractState.context);
            return triggerContexts;
        }

        return null;
    }
    @Override
    public List<String> getTriggeringEntities() {
        List<String> result = new ArrayList<>();
        result.add(entityID);
        return result;
    }
}
