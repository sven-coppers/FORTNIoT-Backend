package sven.phd.iot.api.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

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

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getRules() {
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
}