package sven.phd.iot.students.mathias;

import sven.phd.iot.ContextManager;
import sven.phd.iot.rules.Action;
import sven.phd.iot.students.mathias.states.HassioAction;
import sven.phd.iot.students.mathias.states.HassioConflictSolutionState;
import sven.phd.iot.students.mathias.states.HassioConflictState;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
    public List<HassioAction> getAllActionsOnDevice(@PathParam("id") String id) {
        return ContextManager.getInstance().getAllActionsOnDevice(id);
    }

    /**
     * Get the conflicts in the future.
     * @return Get the conflicts in the future.
     */
    @Path("conflicts/future/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<HassioConflictState> getConflictFuture() {
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
    public List<HassioConflictState> getConflictFuture(@PathParam("id") String id) {
        return ContextManager.getInstance().getFutureConflicts(id);
    }

    /**
     * Get the conflict solutions in the future.
     * @return Get the conflict solutions in the future.
     */
    @Path("conflicts/solutions/future/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<HassioConflictSolutionState> getConflictSolutionsFuture() {
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
    public List<HassioConflictSolutionState> getConflictSolutionsFuture(@PathParam("id") String id) {
        return ContextManager.getInstance().getFutureConflictSolutions(id);
    }
}
