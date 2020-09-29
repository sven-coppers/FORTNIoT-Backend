package sven.phd.iot.api.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.conflicts.Conflict;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("conflicts/")
public class ConflictResource {
    private static ConflictResource conflictResource;

    public ConflictResource() {
        conflictResource = this;
    }

    /**
     * Get the conflicts in the future.
     * @return Get the conflicts in the future.
     */
    @Path("future/")
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
    @Path("future/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Conflict> getConflictFuture(@PathParam("id") String id) {
        return ContextManager.getInstance().getFutureConflicts(id);
    }
}