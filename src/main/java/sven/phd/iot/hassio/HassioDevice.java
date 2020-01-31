package sven.phd.iot.hassio;

import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.BearerToken;
import sven.phd.iot.api.resources.StateResource;
import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.services.HassioService;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;
import sven.phd.iot.hassio.updates.HassioEvent;

import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

abstract public class HassioDevice {
    protected List<HassioState> hassioStateHistory;
    protected List<HassioEvent> hassioEventHistory;
    //  protected List<HassioEvent> hassioEventFuture;
    protected String entityID;
    protected String friendlyName;
    private boolean enabled;
    private boolean available;

    public HassioDevice(String entityID, String friendlyName) {
        this.hassioStateHistory = new ArrayList<>();
        this.hassioEventHistory = new ArrayList<>();
        //    this.hassioEventFuture = new ArrayList<>();
        this.entityID = entityID;
        this.friendlyName = friendlyName;
        this.setEnabled(true);
        this.setAvailable(true);
    }

    /**
     * Get the friendly name of the device
     * @return the friendly name
     */
    public String getFriendlyName() {
        return this.friendlyName;
    }

    /**
     * Commit the state to the physical device
     * @param hassioState
     * @return
     */
    abstract public List<HassioContext> setState(HassioState hassioState);

    /**
     * Get the last known state of this device
     * @return
     */
    public HassioState getLastState() {
        if(this.hassioStateHistory.size() == 0) {
            return null;
        } else {
            return this.hassioStateHistory.get(this.hassioStateHistory.size() - 1);
        }
    }

    /**
     * Process and add the newHassioState to the state history
     * @param hassioStateRaw
     */
    public void logState(HassioStateRaw hassioStateRaw) {
        this.logState(this.processRawState(hassioStateRaw));
    }

    public void logEvent(HassioEvent hassioEvent) {
        this.hassioEventHistory.add(hassioEvent);
    }

    /**
     * Add the newHassioState to the state history
     * @param hassioState
     */
    protected void logState(HassioState hassioState) {
        this.hassioStateHistory.add(hassioState);
        StateResource.getInstance().broadcastState(hassioState);
    }

    /**
     * Get the whole state history of this device
     * @return
     */
    public List<HassioState> getPastStates() {
        return this.hassioStateHistory;
    }

    /**
     * Let the device predict its own future states (without external influences)
     * @return
     */
    abstract protected List<HassioState> predictFutureStates();

    /**
     * Let the deivce predict its own future state changes (without external influences)
     * @return
     */
    public List<HassioChange> predictFutureChanges() {
        List<HassioChange> result = new ArrayList<>();
        List<HassioState> states = new ArrayList<>();

        states.add(this.getLastState());
        states.addAll(this.predictFutureStates());

        for(int i = 1; i < states.size(); i++) {
            result.add(new HassioChange(this.entityID, states.get(i - 1), states.get(i), states.get(i).last_changed));
        }

        return result;
    }

    /**
     * Get the whole event history of this device
     * @return
     */
    public List<HassioEvent> getPastEvents() {
        return this.hassioEventHistory;
    }

    /**
     * Let the device predict its own future state changes (without external influences)
     * @return
     */
    abstract public List<HassioEvent> predictFutureEvents();

    /**
     * Process the raw HassioState to full fledged Java objects (and apply some fixes if needed)
     * @param hassioStateRaw
     * @return
     */
    abstract public HassioState processRawState(HassioStateRaw hassioStateRaw);


    /**
     * Call a service to set the state of this device
     * @param uri
     * @param hassioService
     * @return
     */
    protected List<HassioContext> callService(String uri, HassioService hassioService) {
        List<HassioContext> contexts = new ArrayList<>();
        Client client = ClientBuilder.newClient();

        String servicesUrl = BearerToken.getInstance().getUrl() + "/api/services";
        WebTarget webTarget = client.target(servicesUrl);
        WebTarget employeeWebTarget = webTarget.path(uri);
        Invocation.Builder invocationBuilder = employeeWebTarget.request(MediaType.APPLICATION_JSON);
        //Add authentication
        if(BearerToken.getInstance().isUsingBearer()) {
            String bearer = BearerToken.getInstance().getBearerToken();
            invocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + bearer);
        } else {
            invocationBuilder.header("x-ha-access", "test1234");
        }
        invocationBuilder.header("Content-Type", "application/json");

        try {
            String hassioString = new ObjectMapper().writeValueAsString(hassioService);
            Response response = invocationBuilder.post(Entity.entity(hassioString, MediaType.APPLICATION_JSON));

            //     System.out.println(hassioString);
            //     System.out.println(response.getStatus());
            //     System.out.println("SET STATE RESPONSE: " + response.readEntity(String.class)); // Werkt niet tegelijk met de volgende regel - Je kan maar 1 keer readen

            List<HassioStateRaw> hassioStates = response.readEntity(new GenericType<List<HassioStateRaw>>() {});

            for(HassioState hassioState : hassioStates) {
                contexts.add(hassioState.context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contexts;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}