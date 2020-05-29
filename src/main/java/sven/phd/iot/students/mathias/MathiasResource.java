package sven.phd.iot.students.mathias;

import sven.phd.iot.ContextManager;
import sven.phd.iot.api.request.SimulationRequest;
import sven.phd.iot.predictions.Future;
import sven.phd.iot.rules.Action;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.students.mathias.request.SolutionRequest;
import sven.phd.iot.students.mathias.response.HassioSolutionResponse;
import sven.phd.iot.students.mathias.states.HassioAction;
import sven.phd.iot.students.mathias.states.HassioConflictSolutionState;
import sven.phd.iot.students.mathias.states.HassioConflictState;

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
    public List<HassioAction> getAllActionsOnDevice(@PathParam("id") String id) {
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
}
