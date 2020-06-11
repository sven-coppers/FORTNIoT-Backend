package sven.phd.iot.students.mathias;

import sven.phd.iot.ContextManager;
import sven.phd.iot.rules.Action;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.students.mathias.response.HassioSolutionResponse;
import sven.phd.iot.students.mathias.states.ConflictSolution;
import sven.phd.iot.students.mathias.states.Conflict;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Path("mathias/")
public class MathiasResource {
    private static MathiasResource mathiasResource;

    public MathiasResource() {
        mathiasResource = this;
    }

    /**
     * Singleton
     * @return the single instance of this class
     */
    public static MathiasResource getInstance() {
        if (mathiasResource == null) {
            mathiasResource = new MathiasResource();
        }

        return mathiasResource;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String helloWorld() {
        return "<h1>Hello, Mathias!</h1>";
    }

    /**
     * Get the actions that are possible on the given device
     * @param id
     * @return Get the actions possible on the device.
     */
    @Path("devices/actions/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Action> getAllActionsOnDevice(@PathParam("id") String id) {
        return ContextManager.getInstance().getAllActionsOnDevice(id);
    }

    /**
     * Get the rule that matches the ruleID
     * @param id
     * @return Get the rule.
     */
    @Path("rules/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Trigger getRule(@PathParam("id") String id) {
        return ContextManager.getInstance().getRuleById(id);
    }

    /**
     * Get the action that matches the actionID
     * @param id
     * @return Get the action.
     */
    @Path("actions/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Action getAction(@PathParam("id") String id) {
        return ContextManager.getInstance().getActionById(id);
    }

    /**
     * Get the actions.
     * @return Get the actions.
     */
    @Path("actions/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Action> getActions() {
        return ContextManager.getInstance().getActions();
    }

    /**
     * Get the conflicts in the future.
     * @return Get the conflicts in the future.
     */
    @Path("conflicts/future/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Conflict> getConflictFuture() {
        return ContextManager.getInstance().getFutureConflicts();
    }

    /**
     * Get the conflict in the future of a specific device.
     * @param id
     * @return Get the conflicts in the future.
     */
    @Path("conflicts/future/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Conflict> getConflictFuture(@PathParam("id") String id) {
        return ContextManager.getInstance().getFutureConflicts(id);
    }

    /**
     * Get the conflict solutions in the future.
     * @return Get the conflict solutions in the future.
     */
    @Path("conflicts/solutions/future/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ConflictSolution> getConflictSolutionsFuture() {
        return ContextManager.getInstance().getFutureConflictSolutions();
    }

    /**
     * Get the conflict solutions in the future of a specific device.
     * @param id
     * @return Get the conflict solutions in the future.
     */
    @Path("conflicts/solutions/future/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ConflictSolution> getConflictSolutionsFuture(@PathParam("id") String id) {
        return ContextManager.getInstance().getFutureConflictSolutions(id);
    }

    @Path("conflicts/solution/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public HassioSolutionResponse setConflictSolution(ConflictSolution conflictSolutionRequest)  {
        HassioSolutionResponse response = new HassioSolutionResponse();

        if(ContextManager.getInstance().addConflictSolution(conflictSolutionRequest)) {
            response.success = true;
            response.response = "Successfully added solution";
        } else {
            response.success = false;
            response.response = "something went wrong";
        }
        return response;
    }
}
