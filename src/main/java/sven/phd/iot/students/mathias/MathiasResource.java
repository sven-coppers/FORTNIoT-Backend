package sven.phd.iot.students.mathias;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.states.HassioConflictState;
import sven.phd.iot.hassio.states.HassioState;

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
}
