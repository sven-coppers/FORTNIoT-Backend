package sven.phd.iot.students.bram.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.RuleExecutionEvent;
import sven.phd.iot.rules.Action;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.students.bram.questions.why.WhyResult;
import sven.phd.iot.students.bram.questions.why_not.CurrentState;
import sven.phd.iot.students.bram.questions.why_not.WhyNotResult;
import sven.phd.iot.students.bram.questions.why_not.WhyNotRule;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("/bram/why_not")
public class WhyNotResource {
    @GET
    @Path("/{id}/{state}")
    @Produces(MediaType.APPLICATION_JSON)
    public WhyNotResult why_not(@PathParam("id") String id, @PathParam("state") String state) {
        // Get reason of current state
        WhyNotResult result = new WhyNotResult();
        result.why = getCurrentReason(id);

        // Get the rules that result in the given state
        List<Trigger> rules = getRulesByDeviceAndResult(id, state);
        result.rules = transform(rules);

        return result;
    }
    private List<String> getDeviceIds() {
        ContextManager cm = ContextManager.getInstance();
        Map<String, HassioDevice> map = cm.getHassioDeviceManager().getDevices();

        return new ArrayList<>(map.keySet());
    }

    private String getStateValue(String deviceId) {
        ContextManager cm = ContextManager.getInstance();
        return cm.getHassioDeviceManager().getDevices().get(deviceId).getLastState().state;
    }
    private String getFriendlyName(String deviceId) {
        ContextManager cm =ContextManager.getInstance();
        return cm.getHassioDeviceManager().getDevices().get(deviceId).getFriendlyName();
    }
    public Boolean isTriggered(Trigger t) {
        ContextManager cm = ContextManager.getInstance();
        HashMap<String, HassioState> states = cm.getHassioDeviceManager().getCurrentStates();
        List<HassioState> triggerStates = t.verifyCondition(states);
        return triggerStates != null && triggerStates.size() > 0;
    }

    private List<WhyNotRule> transform(List<Trigger> rules) {
        List<WhyNotRule> result = new ArrayList<>();
        for(Trigger t:rules) {
            WhyNotRule r = new WhyNotRule();
            r.ruleId = t.id;
            r.trigger = t.getTitle();
            r.isTriggered = isTriggered(t);

            List<String> triggeringDevices = t.getTriggeringEntities();
            List<CurrentState> currentStates = new ArrayList<>();
            for(String d:triggeringDevices) {
                CurrentState cs = new CurrentState();
                cs.deviceId = d;
                try {
                    cs.currentState = getStateValue(d);
                    cs.deviceName = getFriendlyName(d);
                } catch(Exception e) {
                    // When device is not found, att an unknown state
                    cs.currentState = "unknown";
                    cs.deviceName = "unknown";
                }
                currentStates.add(cs);
            }

            r.currentStates = currentStates;

            result.add(r);
        }
        return result;
    }
    private List<Trigger> getRulesByDeviceAndResult(String deviceId, String state) {
        ContextManager contextManager = ContextManager.getInstance();
        Map<String, Trigger> rules = contextManager.getRules();
        List<Trigger> result = new ArrayList<Trigger>();
        for(Map.Entry<String, Trigger> entry : rules.entrySet()) {
            Action action = entry.getValue().getActionOnDevice(deviceId);

                List<HassioState> states = action.simulate(new Date(), null);
                for(HassioState s:states) {
                    if(s.state.compareTo(state) == 0) {
                        result.add(entry.getValue());
                    }
                }

        }
        return result;
    }
    private WhyResult getCurrentReason(String id) {
        WhyResource res = new WhyResource();
        return res.why(id);
    }
}
