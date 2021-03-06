package sven.phd.iot.api.resources;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ChunkedOutput;
import sven.phd.iot.ContextManager;
import sven.phd.iot.api.request.SimulationRequest;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;
import sven.phd.iot.predictions.Future;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.*;

@Path("states/")
public class StateResource {
    private static StateResource stateResource;

    public StateResource() {
        stateResource = this;
    }

    /**
     * Singleton
     * @return the single instance of this class
     */
    public static StateResource getInstance() {
        if (stateResource == null) {
            stateResource = new StateResource();
        }

        return stateResource;
    }

    /**
     * Get the state of all devices
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, HassioState> getStates() {
        return ContextManager.getInstance().getHassioStates();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void setDevices(HassioStateRaw[] hassioRawStates) {
        // TODO: Actually apply the new states
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HassioState getDevice(@PathParam("id") String id) {
        return ContextManager.getInstance().getHassioDeviceManager().getCurrentState(id);
    }

    /**
     * Get the history of changes.
     * @return Get the history of changes.
     */
    @Path("history/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HashMap<String, List<HassioState>> getStateHistory() {
        return ContextManager.getInstance().getStateHistory();
    }

    /**
     * Get the history of this specific device
     * @param id
     * @return
     */
    @Path("history/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<HassioState> getDeviceHistory(@PathParam("id") String id) {
        return ContextManager.getInstance().getStateHistory(id);
    }

    /**
     * Get the new SSE change stream channel.
     * @return new SSE change stream channel.
     */
    @Path("stream/")
    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput getChangeStream() {
        //LOGGER.log(Level.INFO, "--> New listener for changes.");

        final EventOutput eventOutput = new EventOutput();

        try {
            eventOutput.write(new OutboundEvent.Builder().name("welcome").data(String.class, "welcome to the state stream").build());
        } catch (IOException e) {
            e.printStackTrace();
        }

        stateBroadcaster.add(eventOutput);

        return eventOutput;
    }

    /**
     * ChangeBroadcaster
     */
    private static SseBroadcaster stateBroadcaster = new SseBroadcaster() {
        @Override
        public void onException(final ChunkedOutput<OutboundEvent> chunkedOutput, final Exception exception) {
            //LOGGER.log(Level.SEVERE, "Error broadcasting new State");
            //exception.printStackTrace();
        }
    };

    /**
     * Broadcast a message to this channel
     * @param hassioState
     */
    public void broadcastState(HassioState hassioState) {
        // Deprecated

       /* final OutboundEvent event = new OutboundEvent.Builder()
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(HassioState.class, hassioState)
                .build();

        stateBroadcaster.broadcast(event); */
    }

    /**
     * Broadcast a message to this channel
     */
    public void broadcastRefresh(String message) {
        final OutboundEvent event = new OutboundEvent.Builder()
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(String.class, message)
                .build();

        stateBroadcaster.broadcast(event);
    }

    /**
     * FOR DEBUG PURPOSES
     * Put a new message to be broadcast to all registered SSE clients
     * @param state message to be broadcast.
     */
    /*@POST
    @Consumes(MediaType.TEXT_PLAIN)
    public void putChange(final String state) {
        LOGGER.info("--> Message received.");
        broadcastState(state);
    } */
}
