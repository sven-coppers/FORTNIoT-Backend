package sven.phd.iot.students.bram.resources;

import com.google.gson.JsonObject;
import org.json.JSONObject;
import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.Action;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.students.bram.questions.why.WhyQuestion;
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
        List<HassioContext> contexts = t.verifyCondition(states);
        return contexts != null && contexts.size() > 0;

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
                cs.currentState = getStateValue(d);
                cs.deviceName = getFriendlyName(d);
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
            List<Action> actions = entry.getValue().getActionOnDevice(deviceId);
            for(Action a:actions) {
                List<HassioState> states = a.simulate(new HassioRuleExecutionEvent(entry.getValue(), new Date(), 0), null);
                for(HassioState s:states) {
                    if(s.state.compareTo(state) == 0) {
                        result.add(entry.getValue());
                    }
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
