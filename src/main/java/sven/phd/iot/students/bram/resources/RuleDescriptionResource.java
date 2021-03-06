package sven.phd.iot.students.bram.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.rules.Action;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.students.bram.questions.why.rule.RuleJson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.awt.*;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/bram/ruledescription")
public class RuleDescriptionResource {
    @GET
    @Path("/{rule_id}/{device_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RuleJson ruleDescription(@PathParam("rule_id") String ruleId, @PathParam("device_id") String deviceId) {
        ContextManager cm = ContextManager.getInstance();
        Trigger rule = cm.getRule(ruleId);

        System.out.println(rule);

        RuleJson result = new RuleJson();
       /* result.rule_id = ruleId;
        result.trigger = rule.getTitle();

        

        Action action = rule.getActionOnDevice(deviceId);
        result.action = action.description; */

        return result;
    }
}
