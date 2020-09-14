package sven.phd.iot.api.resources;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ChunkedOutput;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/** THE WHOLE CLASS IS DEPRECATED */
@Path("updates/")
public class UpdateResource {
    private static UpdateResource updateResource;

    public UpdateResource() {
        updateResource = this;
    }

    /**
     * Singleton
     * @return the single instance of this class
     */
    public static UpdateResource getInstance() {
        if (updateResource == null) {
            updateResource = new UpdateResource();
        }

        return updateResource;
    }

    /**
     * Get all updates (events and state changes), from the past, present, and future
     * @return past, present and future updates (events and state changes)
     */
   /* @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<HassioUpdate> getUpdates() {
        return new ArrayList<>(); // ContextManager.getInstance().getAllUpdates(); // NO LONGER SUPPORTED
    } */

    /**
     * Get the new SSE change stream channel.
     * @return new SSE change stream channel.
     */
    @Path("stream/")
    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput getUpdateStream() {
        //LOGGER.log(Level.INFO, "--> New listener for updates.");
        //Logger.getLogger().info("--> New listener for updates");

        final EventOutput eventOutput = new EventOutput();

        try {
            eventOutput.write(new OutboundEvent.Builder().name("welcome").data(String.class, "welcome to the updpate stream").build());
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateBroadcaster.add(eventOutput);

        return eventOutput;
    }

    /**
     * ChangeBroadcaster
     */
    private static SseBroadcaster updateBroadcaster = new SseBroadcaster() {
        @Override
        public void onException(final ChunkedOutput<OutboundEvent> chunkedOutput, final Exception exception) {
        //LOGGER.log(Level.SEVERE, "Error broadcasting new update");
        //exception.printStackTrace();
        }
    };

    /**
     * Broadcast a message to this channel
     * @param hassioUpdate
     */
   /* public void broadcastUpdate(HassioUpdate hassioUpdate) {
        final OutboundEvent event = new OutboundEvent.Builder()
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(HassioUpdate.class, hassioUpdate)
                .build();

        updateBroadcaster.broadcast(event);
    } */
}