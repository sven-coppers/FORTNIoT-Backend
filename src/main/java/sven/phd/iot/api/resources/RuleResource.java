package sven.phd.iot.api.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.api.request.RuleUpdateRequest;
import sven.phd.iot.hassio.updates.ExecutionEvent;
import sven.phd.iot.hassio.updates.RuleExecutionEvent;
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

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void setRulesProperties(HashMap<String, RuleUpdateRequest> ruleUpdateRequests)  {
        ContextManager contextManager = ContextManager.getInstance();

        for(String ruleID : ruleUpdateRequests.keySet()) {
            RuleUpdateRequest ruleUpdateRequest = ruleUpdateRequests.get(ruleID);

            contextManager.updateRule(ruleID, ruleUpdateRequest.enabled, ruleUpdateRequest.available);
        }
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
    public List<RuleExecutionEvent> getPastRuleExecutions() {
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
    public List<RuleExecutionEvent> getRuleHistory(@PathParam("id") String id) {
        return ContextManager.getInstance().getPastRuleExecutions(id);
    }


    @Path("future/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ExecutionEvent> getFutureRuleExecutions() {
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
    public List<ExecutionEvent> getRuleFuture(@PathParam("id") String id) {
        return ContextManager.getInstance().getFutureRuleExecutions(id);
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setRuleProperties(@PathParam("id") String id, RuleUpdateRequest ruleUpdateRequest)  {
        ContextManager.getInstance().updateRule(id, ruleUpdateRequest.enabled, ruleUpdateRequest.available);
    }
}