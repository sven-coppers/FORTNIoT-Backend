package sven.phd.iot.students.bram.resources;

import org.json.JSONObject;
import sven.phd.iot.ContextManager;
import sven.phd.iot.students.bram.questions.why.WhyQuestion;
import sven.phd.iot.students.bram.questions.why.WhyResult;
import sven.phd.iot.students.bram.questions.why.rule.RuleJson;
import sven.phd.iot.students.bram.questions.why.rule.RuleService;
import sven.phd.iot.students.bram.questions.why.rule.WhyRuleResult;
import sven.phd.iot.students.bram.questions.why.user.WhyUserResult;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/bram/why")
public class WhyResource {
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public WhyResult why(@PathParam("id") String id) {
        boolean becauseOfRule = WhyQuestion.stateBecauseOfRule(id);
        if(becauseOfRule) {
            return becauseOfRule(id);
        } else {
            return becauseOfUser(id);
        }
    }
    private WhyResult becauseOfRule(String deviceId) {
        WhyRuleResult result = new WhyRuleResult();
        result.device_id = deviceId;
        result.actor = "rule";
        String state = ContextManager.getInstance().getHassioState(deviceId).state;
        result.state = state;
        String friendlyName = ContextManager.getInstance().getHassioDeviceManager().getDevice(deviceId).getFriendlyName();
        result.friendly_name = friendlyName;


        RuleJson rule = RuleService.getLastRuleByActionDevice(deviceId);
        if(rule == null) {
            WhyResult unkownResult = new WhyResult();
            unkownResult.device_id = deviceId;
            unkownResult.actor = "unknown";
            unkownResult.state = state;
            unkownResult.friendly_name = friendlyName;
            return unkownResult;
        }
        result.rule_id = rule.rule_id;
        result.rule = rule;
        return result;
    }
    private WhyResult becauseOfUser(String id) {
        WhyUserResult result = new WhyUserResult();
        String state = ContextManager.getInstance().getHassioState(id).state;
        result.device_id = id;
        result.state = state;
        String friendlyName = ContextManager.getInstance().getHassioDeviceManager().getDevice(id).getFriendlyName();
        result.friendly_name = friendlyName;

        result.actor = "user";
        result.user_id = WhyQuestion.getActorID(id);
        result.user = WhyQuestion.getUserActor(id);

        return result;
    }

}
