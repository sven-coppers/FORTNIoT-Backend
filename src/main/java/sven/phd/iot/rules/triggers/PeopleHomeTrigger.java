package sven.phd.iot.rules.triggers;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeopleHomeTrigger extends Trigger {
    private boolean anyoneHome;

    public PeopleHomeTrigger(String ruleIdentifier, boolean anyoneHome) {
        super(ruleIdentifier, anyoneHome? "anyone home" : "nobody home");

        this.anyoneHome = anyoneHome;
    }

    @Override
    public boolean isTriggeredBy(HassioChange hassioChange) {
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

                if(personState != null && personState.state.equals("home")) {
                    triggerContexts.add(personState.context);
                    numPeople++;
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
    @Override
    public List<String> getTriggeringEntities() {
        List<String> result = new ArrayList<>();

        Map<String, HassioDevice> hassioDevices = ContextManager.getInstance().getHassioDeviceManager().getDevices();

        for(String deviceID : hassioDevices.keySet()) {
            if(deviceID.contains("person.")) {
                result.add(deviceID);
            }
        }

        return result;
    }
}