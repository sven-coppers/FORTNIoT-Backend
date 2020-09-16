package sven.phd.iot.rules.triggers;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PeopleHomeTrigger extends Trigger {
    private boolean anyoneHome;

    public PeopleHomeTrigger(String ruleIdentifier, boolean anyoneHome) {
        super(ruleIdentifier, anyoneHome? "anyone home" : "nobody home");

        this.anyoneHome = anyoneHome;
    }

    @Override
    public boolean isTriggeredBy(HashMap<String, HassioState> hassioStates, HassioChange hassioChange) {
        if(hassioChange.entity_id.contains("person.")) {
            return true;
        }

        return false;
    }

    @Override
    public List<HassioContext> verifyCondition(HashMap<String, HassioState> hassioStates) {
        // Count People
        int numPeople = 0;
        List<HassioContext> triggerContexts = new ArrayList<>();

        for(String deviceID : hassioStates.keySet()) {
            if(deviceID.contains("person.")) {
                HassioState personState = hassioStates.get(deviceID);

                if(personState != null) {
                    if(personState.state.equals("home")) {
                        numPeople++;

                        if(anyoneHome) {
                            triggerContexts.add(personState.context);
                        }
                    } else {
                        if(!anyoneHome) {
                            triggerContexts.add(personState.context);
                        }
                    }
                }
            }
        }

        if(anyoneHome && numPeople > 0) {
            return triggerContexts;
        } else if(!anyoneHome && numPeople == 0) {
            return triggerContexts;
        } else {
            return null;
        }
    }
}