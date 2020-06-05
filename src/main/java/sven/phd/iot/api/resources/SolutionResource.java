package sven.phd.iot.api.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.predictions.ConflictSolution;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("solutions/")
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

    /**
     * Get all solutions in the system
     * @return Get the rule.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ConflictSolution> getSolutions() {
        return ContextManager.getInstance().getConflictSolutionManager().getSolutions();
    }
}
