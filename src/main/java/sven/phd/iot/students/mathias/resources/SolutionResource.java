package sven.phd.iot.students.mathias.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.rules.Action;
import sven.phd.iot.students.mathias.response.HassioSolutionResponse;
import sven.phd.iot.students.mathias.states.ConflictSolution;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
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

    // TODO this should be rewritten so it can cope with Custom Action
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public HassioSolutionResponse addConflictSolution(ConflictSolution conflictSolutionRequest)  {
        // TODO for custom action, generate new actionID
        for (Action action : conflictSolutionRequest.getCustomActions()) {
            action.generateNewID();
        }
        ContextManager.getInstance().addConflictSolution(conflictSolutionRequest);

        HassioSolutionResponse response = new HassioSolutionResponse();
        response.success = true;
        response.response = "Successfully added solution";
        return response;
    }

    // TODO ADD removeConflictSolution
    // TODO ADD updateConflictSolution

    /**
     * Get the conflict solutions in the future.
     * @return Get the conflict solutions in the future.
     */
    @Path("future/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ConflictSolution> getConflictSolutionsFuture() {
        return ContextManager.getInstance().getConflictSolutionManager().getSolutions();
    }

    /**
     * Get the conflict solutions in the future of a specific device.
     * @param id
     * @return Get the conflict solutions in the future.
     */
    // TODO implement if necessary
    @Path("future/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ConflictSolution> getConflictSolutionsFuture(@PathParam("id") String id) {
        return new ArrayList<>(); //ContextManager.getInstance().getFutureConflictSolutions(id);
    }
}
