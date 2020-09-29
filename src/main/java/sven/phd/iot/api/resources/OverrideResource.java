package sven.phd.iot.api.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.overrides.SnoozedAction;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("overrides/")
public class OverrideResource {
    private static OverrideResource overrideResource;

    public OverrideResource() {
        overrideResource = this;
    }

    /**
     * Singleton
     * @return the single instance of this class
     */
    public static OverrideResource getInstance() {
        if (overrideResource == null) {
            overrideResource = new OverrideResource();
        }

        return overrideResource;
    }

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
