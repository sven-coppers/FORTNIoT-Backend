package sven.phd.iot.api.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.overrides.SnoozedAction;

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
     * Get a single snoozed action
     * @return the action
     */
    @Path("snoozed_actions/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SnoozedAction getSnoozedAction(@PathParam("id") String id) {
        return ContextManager.getInstance().getOverridesManager().getSnoozedAction(id);
    }

    /**
     * Remove snoozed actions
     */
    @Path("snoozed_actions/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteSnoozedAction(@PathParam("id") String id) {
        ContextManager.getInstance().getOverridesManager().removeSnoozedAction(id);
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
