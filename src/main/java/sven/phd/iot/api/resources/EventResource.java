package sven.phd.iot.api.resources;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ChunkedOutput;
import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.models.*;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Path("events/")
public final class EventResource {
    private static final Logger LOGGER = Logger.getLogger(EventResource.class.getName());
    private static EventResource eventResource;

    public EventResource() {
        eventResource = this;
    }

    /**
     * Singleton
     * @return the single instance of this class
     */
    public static EventResource getInstance() {
        if (eventResource == null) {
            eventResource = new EventResource();
        }

        return eventResource;
    }

    /**
     * Get the history of events.
     * @return Get the history of events.
     */
    @Path("history/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Event> getEventHistory() {
        return EventHistory.getInstance().getHistory();
    }

    /**
     * Get the predicted events.
     * @return Get the predicted events.
     */
    @Path("future/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public List<HassioChange> previewChanges() {
        return null; // TODO: Return the future events queue
    }

    /**
     * Get the new SSE events stream channel.
     * @return new SSE events stream channel.
     */
    @Path("stream/")
    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput getEventStream() {
        LOGGER.info("--> New listener for events.");
        final EventOutput eventOutput = new EventOutput();

        try {
            eventOutput.write(new OutboundEvent.Builder().name("welcome").data(String.class, "welcome to the event stream").build());
        } catch (IOException e) {
            e.printStackTrace();
        }

        eventBroadcaster.add(eventOutput);

        return eventOutput;
    }

    /**
     * Get the history of changes.
     * @return Get the history of changes.
     */
   /* @Path("history/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<HassioEvent> getEventHistory() {
        return HassioClient.getInstance().getHassioEventHistory();
    }*.

    /**
     * Get the future of changes.
     * @return Get the future of changes.
     */
/*    @Path("future/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<HassioEvent> getEventeFuture() {
        List<HassioChange> hassioChanges = HassioClient.getInstance().getFutureStates();
        List<HassioState> hassioStateList = new ArrayList<HassioState>();

        for(HassioChange hassioChange : hassioChanges) {
            hassioStateList.add(hassioChange.hassioChangeData.newState);
        }

        Collections.sort(hassioStateList);

        return hassioStateList;
    }*/

    /**
     * ChangeBroadcaster
     */
    private static SseBroadcaster eventBroadcaster = new SseBroadcaster() {
        @Override
        public void onException(final ChunkedOutput<OutboundEvent> chunkedOutput, final Exception exception) {
        LOGGER.log(Level.SEVERE, "Error broadcasting change");
        System.err.println(exception);
        }
    };

    /**
     * Broadcast a message to this channel
     * @param event
     */
    public void broadcastEvent(Event event) {
        final OutboundEvent outboundEvent = new OutboundEvent.Builder()
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(Event.class, event)
                .build();

        eventBroadcaster.broadcast(outboundEvent);
    }

    /**
     * Really execute event
     **/
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, HassioState> confirmEvent(Event event) {
       /* Snapshot snapshot = HassioClient.getSnapshot(); // --> Devices getStates
        RulesManager rulesManager = RulesManager.getInstance();
        rulesManager.consumeEvent(event, snapshot, RulesManager.EFFECT.CONFIRM);

        List<String> contexts = HassioClient.setSnapshot(snapshot);
        event.contexts.addAll(contexts);
        EventHistory.getInstance().addEvent(event);

        return snapshot.getHassioStateMap(); */
       return null;
    }

    /**
     * Get feedforward about the outcome of an event event
     **/
    @Path("/preview")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, HassioState> previewEvent(Event event) {
       /* Snapshot snapshot = HassioClient.getSnapshot();
        RulesManager rulesManager = RulesManager.getInstance();
        rulesManager.consumeEvent(event, snapshot, RulesManager.EFFECT.PREVIEW);

        //TODO: Generate a new Context

        return snapshot.getHassioStateMap(); */
         return null;
    }
}