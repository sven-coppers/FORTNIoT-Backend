package sven.phd.iot.api.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.api.request.RuleEnabledRequest;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.Trigger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("rules/")
public class RuleResource {
    private static RuleResource ruleResource;

    public RuleResource() {
        ruleResource = this;
    }

    /**
     * Singleton
     * @return the single instance of this class
     */
    public static RuleResource getInstance() {
        if (ruleResource == null) {
            ruleResource = new RuleResource();
        }

        return ruleResource;
    }

    @Path("text/")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String printRules() {
        return ContextManager.getInstance().printRules();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Trigger> getRuleStates() {
        return ContextManager.getInstance().getRules();
    }


    @Path("history/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<HassioRuleExecutionEvent> getPastRuleExecutions() {
        return ContextManager.getInstance().getPastRuleExecutions();
    }

    /**
     * Get the history of this specific rule
     * @param id
     * @return
     */
    @Path("history/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<HassioRuleExecutionEvent> getRuleHistory(@PathParam("id") String id) {
        return ContextManager.getInstance().getPastRuleExecutions(id);
    }


    @Path("future/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<HassioRuleExecutionEvent> getFutureRuleExecutions() {
        return ContextManager.getInstance().getFutureRuleExecutions();
    }

    /**
     * Get the future of this specific rule
     * @param id
     * @return
     */
    @Path("future/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<HassioRuleExecutionEvent> getRuleFuture(@PathParam("id") String id) {
        return ContextManager.getInstance().getFutureRuleExecutions(id);
    }

    @Path("{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void setRuleEnabled(@PathParam("id") String id, RuleEnabledRequest ruleEnabledRequest)  {
        ContextManager.getInstance().updateRule(id, ruleEnabledRequest.enabled);
    }
}