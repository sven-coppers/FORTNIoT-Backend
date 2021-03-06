package sven.phd.iot.hassio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;
import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import sven.phd.iot.BearerToken;
import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.bus.HassioBus;
import sven.phd.iot.hassio.calendar.HassioCalendar;
import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.change.HassioChangeRaw;
import sven.phd.iot.hassio.light.HassioLight;
import sven.phd.iot.hassio.moon.HassioMoon;
import sven.phd.iot.hassio.outlet.HassioOutlet;
import sven.phd.iot.hassio.person.HassioPerson;
import sven.phd.iot.hassio.sensor.HassioBattery;
import sven.phd.iot.hassio.sensor.HassioBinarySensor;
import sven.phd.iot.hassio.sensor.HassioSensor;
import sven.phd.iot.hassio.services.HassioCallService;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;
import sven.phd.iot.hassio.sun.HassioSun;
import sven.phd.iot.hassio.tracker.HassioDeviceTracker;
import sven.phd.iot.predictions.Future;
import sven.phd.iot.rules.RuleExecution;
import sven.phd.iot.hassio.weather.HassioWeather;
import sven.phd.iot.students.bram.questions.why.user.UserService;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.*;

public class HassioDeviceManager implements EventListener {
    private String HASSIO_URL;
    private HashMap<String, HassioDevice> hassioDeviceMap;
    private ContextManager contextManager;
    private Client client;
    private WebTarget target;
    private EventSource eventSource;

    public HassioDeviceManager(ContextManager contextManager) {
        this.hassioDeviceMap = new HashMap<>();
        this.eventSource = null;
        this.HASSIO_URL = null;

        this.contextManager = contextManager;
    }

    public void addDevice(HassioDevice device) {
        this.hassioDeviceMap.put(device.entityID, device);
    }

    public void startListening() {
        if(isConnectedToHassioInstance()) return;
        System.out.println("HassioDeviceManager - Connecting to Hassio Instance...");

        this.HASSIO_URL = BearerToken.getInstance().getUrl() + "/api/";

        this.initialiseHassioDevices();

        try {
            BearerToken bearerToken = BearerToken.getInstance();

            client = ClientBuilder.newBuilder().register(SseFeature .class).build();
            if(bearerToken.isUsingBearer()) {
                Feature feature = OAuth2ClientSupport.feature(bearerToken.getBearerToken());
                client.register(feature);
                target = client.target(HASSIO_URL + "stream");
            } else {
                target = client.target(HASSIO_URL + "stream?api_password=test1234");
            }

            eventSource = EventSource.target(target).build();
            eventSource.register(contextManager.getHassioDeviceManager()); // Everything needs to go to a single listener, since Hassio does not support event types
            eventSource.open();

            //Fetch users from the server
            UserService.getInstance();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void stopListening() {
        eventSource.close();
        System.out.println("HassioDeviceManager - Disconnected from Hassio Instance");
    }

    /**
     * Choose which devices we will keep track of
     */
    public void initialiseHassioDevices() {
        System.out.println("HassioDeviceManager - Initialising Hassio Devices...");
        List<HassioStateRaw> hassioStateRawList = this.queryHassioStatesRaw();

        for(HassioStateRaw hassioStateRaw : hassioStateRawList) {
            String entity_id = hassioStateRaw.entity_id;
            String friendlyName = "UNKNOWN";

            if(hassioStateRaw.attributes != null && hassioStateRaw.attributes.get("friendly_name") != null) {
                friendlyName = hassioStateRaw.attributes.get("friendly_name").asText();
            }

            HassioDevice device = null;

            if(entity_id.contains("sun.")) {
                device = new HassioSun();
            } else if(entity_id.contains("device_tracker.")) {
                device = new HassioDeviceTracker(entity_id, friendlyName);
            } else if(entity_id.contains("switch.")) {
                device = new HassioOutlet(entity_id, friendlyName);
            } else if(entity_id.contains("person.")) {
                device = new HassioPerson(entity_id, friendlyName);
            } else if(entity_id.contains(".updater")) {
                // Ignore
                continue;
            } else if(entity_id.contains("binary_sensor")) {
                if(entity_id.contains("motion_sensor_motion")) {
                    device = new HassioBinarySensor(entity_id, friendlyName);
                } else if(entity_id.contains("remote_ui")) {
                    device = new HassioBinarySensor(entity_id, friendlyName);
                } else if(entity_id.contains("_contact")) {
                    device = new HassioBinarySensor(entity_id, friendlyName);
                } else if(entity_id.contains("_acceleration")) {
                    device = new HassioBinarySensor(entity_id, friendlyName);
                }
            } else if(entity_id.contains("sensor.")) {
                if(entity_id.contains("sensor.yr_symbol")) continue; // Ignore
                if(entity_id.contains("sensor.moon")) {
                    device = new HassioMoon();
                } else if(entity_id.contains("sensor.agoralaan_diepenbeek")) {
                    device = new HassioBus(entity_id, "Bus Stop - Agoralaan");
                } else if(entity_id.contains("temperature_measurement")) {
                    device = new HassioSensor(entity_id, friendlyName);
                } else if(entity_id.contains("battery")) { // battery_level
                    device = new HassioBattery(entity_id, friendlyName);
               // } else if(entity_id.contains("battery")) {
              //      device = new HassioSensor(entity_id, friendlyName);
                } else if(entity_id.contains("_coordinate")) {
                    device = new HassioSensor(entity_id, friendlyName);
                }
            } else if(entity_id.contains("light.")) {
                device = new HassioLight(entity_id, friendlyName);
            } else if (entity_id.contains("weather.dark_sky")) {
                device = new HassioWeather(entity_id, friendlyName);
            } else if(entity_id.contains("automation.")) {
                continue; // Ignore
            }  else if(entity_id.contains("calendar.")) {
                device = new HassioCalendar(entity_id, friendlyName);
            } else if(entity_id.contains("group.")) {
                // TODO
                continue;
            } else if(entity_id.contains("zone.")) {
                // TODO
                continue;
            } else if(entity_id.contains("scene.")) {
                continue; // Ignore
            } else if(entity_id.contains("persistent_notification.")) {
                continue; // Ignore
            }

            if(device != null) {
                device.logState(hassioStateRaw);
                this.hassioDeviceMap.put(entity_id, device);
            } else {
                System.err.println("HassioDeviceManager - Device detected that Sven does not support yet: " + entity_id);

                try {
                    System.err.println(new ObjectMapper().writeValueAsString(hassioStateRaw));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("HassioDeviceManager - " + this.hassioDeviceMap.keySet().size() + " devices found");
    }

    /**
     * Ask Home Assistant for the current states of all devices
     * @return
     */
    private List<HassioStateRaw> queryHassioStatesRaw() {
        Client client = ClientBuilder.newClient();

        WebTarget webTarget = client.target(HASSIO_URL);
        WebTarget employeeWebTarget = webTarget.path("states");
        Invocation.Builder invocationBuilder = employeeWebTarget.request(MediaType.APPLICATION_JSON);

        //Add authentication
        if(BearerToken.getInstance().isUsingBearer()) {
            String bearer = BearerToken.getInstance().getBearerToken();
            invocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + bearer);
        } else {
            invocationBuilder.header("x-ha-access", "test1234");
        }

        Response response = invocationBuilder.get();

        List<HassioStateRaw> hassioStates = response.readEntity(new GenericType<List<HassioStateRaw>>() {});

        return hassioStates;
    }

    /**
     * Ask Home Assistant to perform some state changes
     * @param states
     * @return
     */
    public List<HassioContext> setHassioDeviceStates(List<HassioState> states) {
        List<HassioContext> contexts = new ArrayList<>();

        for(HassioState state : states) {
            contexts.addAll(this.hassioDeviceMap.get(state.entity_id).setState(state));
        }

        return contexts;
    }

    /**
     * NEW: Give devices a chance to predict WHEN RuleExecutions will happen (e.g. turn heater on/off)...
     * @param newDate
     * @param future
     * @return
     */
    public List<HassioState> predictLayerFutureStates(Date newDate, Future future){
        List<HassioState> results = new ArrayList<>();

        for(String entityID : hassioDeviceMap.keySet()) {
            if(!hassioDeviceMap.get(entityID).isEnabled()) continue;

            results.addAll(hassioDeviceMap.get(entityID).predictLayerFutureStates(newDate, future));
        }

        return results;
    }

    /**
     * NEW: predict WHEN the state will change, based on current the state of all devices in the current frame (e.g. update temperature)...
     * This function is called only once per frame per device
     * @return
     */
    public List<HassioState> predictTickFutureStates(Date newDate, Future future) {
        List<HassioState> results = new ArrayList<>();

        for(String entityID : hassioDeviceMap.keySet()) {
            if(!hassioDeviceMap.get(entityID).isEnabled()) continue;

            results.addAll(hassioDeviceMap.get(entityID).predictTickFutureStates(newDate, future));
        }

        return results;
    }

    /**
     * Called everytime HASSIO sends an event
     * @param inboundEvent
     */
    public void onEvent(InboundEvent inboundEvent) {
        String message = inboundEvent.readData(String.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();

        try {
            if(message.equals("ping")) {
                // Ignore
            } else if(message.contains("\"event_type\": \"call_service\"")) {
                // Ignore service calls
                // System.out.println(message);

                HassioCallService hassioCallService = mapper.readValue(message, HassioCallService.class);
                // System.out.println(hassioCallService.toString());
            } else if(message.contains("\"event_type\": \"persistent_notifications_updated\"")) {
            } else if(message.contains("\"event_type\": \"smartthings.button\"")) {
                // Ignore service calls
                // System.out.println(message);

                HassioCallService hassioCallService = mapper.readValue(message, HassioCallService.class);
                // System.out.println(hassioCallService.toString());
            } else if(message.contains("\"event_type\": \"state_changed\"")){
                HassioChangeRaw hassioChangeRaw = mapper.readValue(message, HassioChangeRaw.class);
                String entityID = hassioChangeRaw.hassioChangeData.entityId;

                // Cast hassioChangeRaw to hassioChange
                HassioState oldState = this.hassioDeviceMap.get(entityID).processRawState(hassioChangeRaw.hassioChangeData.oldState);
                HassioState newState = this.hassioDeviceMap.get(entityID).processRawState(hassioChangeRaw.hassioChangeData.newState);

                HassioChange hassioChange = new HassioChange(entityID, oldState, newState, hassioChangeRaw.timeFired);

                this.stateChanged(hassioChange);
            } else {
                System.err.println(message);
            }
        } catch (IOException e) {
            System.err.println(message);
            e.printStackTrace();
        }
    }

    /**
     * To be called by Home assistant, or by stateScheduler
     */
    private void stateChanged(HassioChange hassioChange) {
        if(this.hassioDeviceMap.containsKey(hassioChange.entity_id)) {
            this.hassioDeviceMap.get(hassioChange.entity_id).logState(hassioChange.hassioChangeData.newState);

            // Check if something needs to happen
            this.contextManager.deviceChanged(hassioChange);
        } else {
            System.err.println("Ignored incoming change");
        }
    }

    /**
     * To be called by stateScheduler
     */
    public void stateChanged(HassioState newState) {
        if(this.hassioDeviceMap.containsKey(newState.entity_id)) {
            HassioState oldState = this.hassioDeviceMap.get(newState.entity_id).getLastState();
            HassioChange hassioChange = new HassioChange(newState.entity_id, oldState, newState, newState.getLastChanged());

            this.stateChanged(hassioChange);
        }
    }

    /** Log a state without triggering rules
     * To be called by StudyStates
     * @param newState
     */
    public void logState(HassioState newState) {
        if(this.hassioDeviceMap.containsKey(newState.entity_id)) {
            this.hassioDeviceMap.get(newState.entity_id).logState(newState);
        } else {
            System.err.println("Could not log state for " + newState.entity_id);
        }
    }

    public HassioState getCurrentState(String id) {
        return hassioDeviceMap.get(id).getLastState();
    }

    public List<HassioState> castRawStates(List<HassioStateRaw> hassioStateRawList) {
        List<HassioState> result = new ArrayList<>();

        for(HassioStateRaw hassioStateRaw : hassioStateRawList) {
            String entityID = hassioStateRaw.entity_id;

            result.add(this.hassioDeviceMap.get(entityID).processRawState(hassioStateRaw));
        }

        return result;
    }

    public HashMap<String, HassioState> getCurrentStates() {
        HashMap<String, HassioState> result = new HashMap<>();

        for(String entityID : hassioDeviceMap.keySet()) {
            result.put(entityID, hassioDeviceMap.get(entityID).getLastState());
        }

        return result;
    }

    /**
     * Get the history of all device states
     * @return
     */
    public HashMap<String, List<HassioState>> getStateHistory() {
        HashMap<String, List<HassioState>> hassioStates = new HashMap<>();

        for(String entityID : hassioDeviceMap.keySet()) {
            hassioStates.put(entityID, hassioDeviceMap.get(entityID).getPastStates());
        }


        return hassioStates;
    }

    /**
     * Get the history states of each device
     * @return
     */
    public List<HassioState> getStateHistory(String id) {
        List<HassioState> hassioStates = new ArrayList<>();

        if(hassioDeviceMap.containsKey(id)) {
            hassioStates.addAll(hassioDeviceMap.get(id).getPastStates());
        }

        Collections.sort(hassioStates);

        return hassioStates;
    }

    /**
     * Predict future states, based on the devices internal knowledge
     * This function is called ONCE EVERY SIMULATION, AT THE VERY BEGINNING
     * @return
     */
    public List<HassioState> predictFutureStates() {
        List<HassioState> hassioStates = new ArrayList<>();

        for(String entityID : hassioDeviceMap.keySet()) {
            if(!hassioDeviceMap.get(entityID).isEnabled()) continue;

            hassioStates.addAll(hassioDeviceMap.get(entityID).predictInitialFutureStates());
        }

        Collections.sort(hassioStates);

        return hassioStates;
    }

    /**
     * Get the Hassio device itself
     * @param entityID
     * @return the Hassio device itself
     */
    public HassioDevice getDevice(String entityID) {
        return this.hassioDeviceMap.get(entityID);
    }

    /**
     * Get the hassio devices
     * @return the hassio devices
     */
    public HashMap<String, HassioDevice> getDevices() {
        return this.hassioDeviceMap;
    }

    public boolean isConnectedToHassioInstance() {
        if(eventSource == null) return false;

        return eventSource.isOpen();
    }

    public void setAllDevicesAvailable(boolean available) {
        for(String deviceID : this.hassioDeviceMap.keySet()) {
            hassioDeviceMap.get(deviceID).setAvailable(available);
        }
    }

    public void setDeviceEnabled(String deviceID, boolean enabled) {
        if(hassioDeviceMap.containsKey(deviceID)) {
            hassioDeviceMap.get(deviceID).setEnabled(enabled);
        }
    }

    public void setDeviceAvailable(String deviceID, boolean available) {
        if(hassioDeviceMap.containsKey(deviceID)) {
            hassioDeviceMap.get(deviceID).setAvailable(available);
        }
    }

    public void scheduleState(HassioState hassioState) {
        if(this.hassioDeviceMap.containsKey(hassioState.entity_id)) {
            this.hassioDeviceMap.get(hassioState.entity_id).scheduleState(hassioState);
        } else {
            System.err.println("Could not schedule state' " + hassioState.state + "' for device '" + hassioState.entity_id + "'");
        }
    }

    public void clearLogs() {
        for(String deviceID : this.hassioDeviceMap.keySet()) {
            hassioDeviceMap.get(deviceID).clearHistory();
        }
    }

    public void setAllDevicesEnabled(boolean enabled) {
        for(String deviceID : this.hassioDeviceMap.keySet()) {
            hassioDeviceMap.get(deviceID).setEnabled(enabled);
        }
    }

    public void clearScheduledStates() {
        for(String deviceID : this.hassioDeviceMap.keySet()) {
            hassioDeviceMap.get(deviceID).clearScheduledStates();
        }
    }
}