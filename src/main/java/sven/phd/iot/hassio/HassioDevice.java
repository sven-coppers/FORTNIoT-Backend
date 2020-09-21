package sven.phd.iot.hassio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.BearerToken;
import sven.phd.iot.api.resources.StateResource;
import sven.phd.iot.hassio.services.HassioService;
import sven.phd.iot.hassio.states.*;
import sven.phd.iot.predictions.Future;
import sven.phd.iot.rules.Action;

import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

abstract public class HassioDevice {
    protected List<HassioState> hassioStateHistory;
    @JsonProperty("entity_id") protected String entityID;
    @JsonProperty("friendly_name") protected String friendlyName;
    @JsonProperty("enabled") private boolean enabled;
    @JsonProperty("available") private boolean available;
    @JsonProperty("change_cooldown") private long changeCoolDown; // in milliseconds
    @JsonProperty("redundancy_bad") private boolean redundancyBad; //

    public HassioDevice(String entityID, String friendlyName) {
        this.hassioStateHistory = new ArrayList<>();
        this.entityID = entityID;
        this.friendlyName = friendlyName;
        this.setEnabled(true);
        this.setAvailable(true);
        this.setChangeCoolDown(5 * 60 * 1000); //
        this.setRedundancyBad(true);
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
    public List<HassioContext> setState(HassioState hassioState) {
        return new ArrayList<>();
    }

    /**
     * Get the last known state of this device
     * @return
     */
    @JsonIgnore
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

    /**
     * Add the newHassioState to the state history
     * @param hassioState
     */
    public void logState(HassioState hassioState) {
        this.hassioStateHistory.add(hassioState);
        StateResource.getInstance().broadcastState(hassioState);
    }

    /**
     * Get the whole state history of this device
     * @return
     */
    @JsonIgnore
    public List<HassioState> getPastStates() {
        return this.hassioStateHistory;
    }

    /**
     * Let the device predict its own future states (without external influences)
     * This function is called ONCE EVERY SIMULATION, AT THE VERY BEGINNING
     * @return
     */
    protected List<HassioState> predictInitialFutureStates() {
        return new ArrayList<>();
    }

    /**
     * NEW: predict WHEN the state will change, based on current the state of all devices in the current frame (e.g. update temperature)...
     * This function is called only ONCE EVERY FRAME
     * @return
     * @post when the function is complete, the RuleExecutions are added to the future
     */
    protected List<HassioState> predictTickFutureStates(Date newDate, Future future) {
        return new ArrayList<>(); // Most devices do not change on other devices
    }

    /**
     * NEW: Give devices a chance to predict RuleExecutions (e.g. turn heater on/off) ...
     * This function is called ONCE EVERY LAYER
     * @param newDate
     * @param future
     * @return
     * @post when the function is complete, the RuleExecutions are added to the future
     */
    protected List<HassioState> predictLayerFutureStates(Date newDate, Future future){
        return new ArrayList<>(); // Most devices do not change on other devices
    }

    /**
     * Process the raw HassioState to full fledged Java objects (and apply some fixes if needed)
     * @param hassioStateRaw
     * @return
     */
    public HassioState processRawState(HassioStateRaw hassioStateRaw) {
        try {
            HassioAttributes hassioAttributes = processRawAttributes(hassioStateRaw.attributes);
            return new HassioState(hassioStateRaw, hassioAttributes);
        } catch (IOException e) {
            System.err.println("Could not process the attributes in " + hassioStateRaw.attributes.toString());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Process the raw json for attributes of a state
     * @param rawAttributes
     * @return
     * @throws IOException
     */
    abstract public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException;

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

            for(HassioStateRaw hassioState : hassioStates) {
                contexts.add(hassioState.context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contexts;
    }

    public void clearHistory() {
        this.hassioStateHistory.clear();
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

    @JsonIgnore
    public List<Action> getAllActions(){
        return new ArrayList<>();
    }

    public long getChangeCoolDown() {
        return changeCoolDown;
    }

    public void setChangeCoolDown(long changeCoolDown) {
        this.changeCoolDown = changeCoolDown;
    }

    public boolean isRedundancyBad() {
        return redundancyBad;
    }

    public void setRedundancyBad(boolean redundancyBad) {
        this.redundancyBad = redundancyBad;
    }
}