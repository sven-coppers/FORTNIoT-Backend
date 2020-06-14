package sven.phd.iot.students.mathias.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.rules.Action;
import sven.phd.iot.rules.Trigger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

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
    public List<Action> getActions() {
        return new ArrayList<>(ContextManager.getInstance().getActions().values());
    }
}
