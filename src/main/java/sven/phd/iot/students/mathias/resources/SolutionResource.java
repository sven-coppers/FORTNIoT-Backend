package sven.phd.iot.students.mathias.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.students.mathias.response.HassioSolutionResponse;
import sven.phd.iot.students.mathias.states.HassioConflictSolutionState;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("mathias/solutions/")
public class SolutionResource {
    private static SolutionResource solutionResource;

    public SolutionResource() {
        solutionResource = this;
    }

    /**
     * Singleton
     * @return the single instance of this class
     */
    public static SolutionResource getInstance() {
        if (solutionResource == null) {
            solutionResource = new SolutionResource();
        }

        return solutionResource;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public HassioSolutionResponse setConflictSolution(HassioConflictSolutionState conflictSolutionRequest)  {
        HassioSolutionResponse response = new HassioSolutionResponse();

        if(ContextManager.getInstance().addConflictSolution(conflictSolutionRequest)) {
            response.success = true;
            response.response = "Successfully added solution";
        } else {
            response.success = false;
            response.response = "Something went wrong";
        }
        return response;
    }

    /**
     * Get the conflict solutions in the future.
     * @return Get the conflict solutions in the future.
     */
    @Path("future/")
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
    @Path("future/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<HassioConflictSolutionState> getConflictSolutionsFuture(@PathParam("id") String id) {
        return ContextManager.getInstance().getFutureConflictSolutions(id);
    }
}
