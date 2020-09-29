package sven.phd.iot.api.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.api.request.SimulationRequest;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.Future;
import sven.phd.iot.students.mathias.states.SnoozedAction;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("overrides/")
public class OverrideResource {
    /**
     * Get snoozed actions
     * @return Get all snoozed actions
     */
    @Path("snoozed_actions/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SnoozedAction> getSnoozedActions() {
        return ContextManager.getInstance().getOverridesManager().getSnoozedActions();
    }

    /**
     * Add a snoozed action
     */
    @Path("snoozed_actions/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addSnoozedAction(SnoozedAction snoozedAction) {
        ContextManager.getInstance().getOverridesManager().addSnoozedAction(snoozedAction);
    }

    /**
     * Get all custom actions
     * @return all custom actions
     */
    @Path("custom_states/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<HassioState> getCustomActions() {
        return ContextManager.getInstance().getOverridesManager().getScheduledStates();
    }
}
