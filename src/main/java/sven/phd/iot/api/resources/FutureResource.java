package sven.phd.iot.api.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.Future;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
}
