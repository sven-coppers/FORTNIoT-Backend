package sven.phd.iot.api.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.api.request.SimulationRequest;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.Future;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path("future/")
public class FutureResource {
    /**
     * Get the future of the smart home
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Future getStates() {
        return ContextManager.getInstance().getFuture();
    }

    @Path("simulate/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Future simulateAlternativeFuture(SimulationRequest simulationRequest)  {
        return ContextManager.getInstance().getPredictionEngine().whatIf(simulationRequest);
    }
}
