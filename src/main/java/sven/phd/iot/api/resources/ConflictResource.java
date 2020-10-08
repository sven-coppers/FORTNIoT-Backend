package sven.phd.iot.api.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.conflicts.Conflict;
import sven.phd.iot.conflicts.ConflictVerificationManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
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

    @Path("verifiers/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HashMap<String, Boolean> getVerifiers() {
        ConflictVerificationManager conflictVerificationManager = ContextManager.getInstance().getConflictVerificationManager();
        HashMap<String, Boolean> result = new HashMap<>();

        for(String verifierID: conflictVerificationManager.getAllVerifiers()) {
            result.put(verifierID, false);
        }

        for(String verifierID: conflictVerificationManager.getActiveVerifiers()) {
            result.put(verifierID, true);
        }

        return result;
    }

    @PUT
    @Path("verifiers/")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setVerifiers(HashMap<String, Boolean> enabledVerifiers) {
        ConflictVerificationManager conflictVerificationManager = ContextManager.getInstance().getConflictVerificationManager();

        for(String verifierID : enabledVerifiers.keySet()) {
            conflictVerificationManager.setVerifierEnabled(verifierID, enabledVerifiers.get(verifierID));
        }
        ContextManager.getInstance().getPredictionEngine().updateFuturePredictions();
    }
}
