package sven.phd.iot.hassio;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.InboundEvent;
import sven.phd.iot.BearerToken;
import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.bus.HassioBus;
import sven.phd.iot.hassio.calendar.HassioCalendar;
import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.change.HassioChangeRaw;
import sven.phd.iot.hassio.light.HassioLight;
import sven.phd.iot.hassio.outlet.HassioOutlet;
import sven.phd.iot.hassio.sensor.HassioBinarySensor;
import sven.phd.iot.hassio.sensor.HassioSensor;
import sven.phd.iot.hassio.services.HassioCallService;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;
import sven.phd.iot.hassio.sun.HassioSun;
import sven.phd.iot.hassio.tracker.HassioDeviceTracker;
import sven.phd.iot.hassio.updates.HassioEvent;
import sven.phd.iot.hassio.weather.HassioWeather;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

public class HassioDeviceManager implements EventListener {
    private static String HASSIO_URL = "http://hassio.local:8123/api/";
    private static Map<String, HassioDevice> hassioDeviceMap;
    private ContextManager contextManager;

    public HassioDeviceManager(ContextManager contextManager) {
        System.out.println("Initiating HassioDeviceManager ...");
        this.contextManager = contextManager;
        this.initialiseDevices();
    }

    /**
     * Choose which devices we will keep track of
     */
    public void initialiseDevices() {
        this.hassioDeviceMap = new HashMap<String, HassioDevice>();
        List<HassioStateRaw> hassioStateRawList = this.queryHassioStatesRaw();

        for(HassioStateRaw hassioStateRaw : hassioStateRawList) {
            String entity_id = hassioStateRaw.entity_id;
            HassioDevice device = null;

            if(entity_id.contains("sun.")) {
                device = new HassioSun();
            } else if(entity_id.contains("device_tracker.")) {
                device = new HassioDeviceTracker(entity_id);
            } else if(entity_id.contains("switch.")) {
                device = new HassioOutlet(entity_id);
            } else if(entity_id.contains(".updater")) {
                // Ignore
                continue;
            } else if(entity_id.contains("binary_sensor")) {
                if(entity_id.contains("motion_sensor_motion")) {
                    device = new HassioBinarySensor(entity_id);
                } else if(entity_id.contains("remote_ui")) {
                    device = new HassioBinarySensor(entity_id);
                }
            } else if(entity_id.contains("sensor.")) {
                if(entity_id.contains("sensor.yr_symbol")) continue; // Ignore

                if(entity_id.contains("sensor.agoralaan_diepenbeek")) {
                    device = new HassioBus(entity_id);
                } else if(entity_id.contains("temperature_measurement")) {
                    device = new HassioSensor(entity_id);
                } else if(entity_id.contains("battery")) {
                    device = new HassioSensor(entity_id);
                }
            } else if(entity_id.contains("light.")) {
                device = new HassioLight(entity_id);
            } else if (entity_id.contains("weather.dark_sky")) {
                device = new HassioWeather(entity_id);
            } else if(entity_id.contains("automation.")) {
                continue; // Ignore
            }  else if(entity_id.contains("calendar.")) {
                 if(entity_id.contains("calendar.sven_coppers_uhasselt_be")) {
                     device = new HassioCalendar(entity_id);
                } else {
                     continue; // Ignore
                }
            } else if(entity_id.contains("group.")) {
                // TODO
                continue;
            } else if(entity_id.contains("zone.")) {
                // TODO
                continue;
            }

            if(device != null) {
                device.logState(hassioStateRaw);
                this.hassioDeviceMap.put(entity_id, device);
            } else {
                System.err.println("Device detected that Sven does not support yet: " + entity_id);

                /*try {
                    System.err.println(new ObjectMapper().writeValueAsString(hassioStateRaw));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                } */
            }
        }
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
     * @param hassioStates
     * @return
     */
    public List<HassioContext> setHassioDeviceStates(List<HassioState> hassioStates) {
        List<HassioContext> contexts = new ArrayList<>();

        for(HassioState hassioState : hassioStates) {
            contexts.addAll(this.hassioDeviceMap.get(hassioState.entity_id).setState(hassioState));
        }

        return contexts;
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

                if(this.hassioDeviceMap.containsKey(entityID)) {
                    this.hassioDeviceMap.get(entityID).logState(newState);

                    // Check if something needs to happen
                    this.contextManager.deviceChanged(hassioChange);
                } else {
                    System.out.println("Ignored change: " + message);
                }
            } else {
                System.err.println(message);
            }
        } catch (IOException e) {
            System.err.println(message);
            e.printStackTrace();
        }
    }

    public HassioState getCurrentState(String id) {
        return hassioDeviceMap.get(id).getLastState();
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
    public List<HassioState> getStateHistory() {
        List<HassioState> hassioStates = new ArrayList<>();

        for(String entityID : hassioDeviceMap.keySet()) {
            hassioStates.addAll(hassioDeviceMap.get(entityID).getPastStates());
        }

        Collections.sort(hassioStates);

        return hassioStates;
    }

    /**
     * Get the cached version of the future states of each device
     * @return
     */
    public List<HassioState> getStateFuture() {
        List<HassioState> hassioStates = new ArrayList<>();

        for(String entityID : hassioDeviceMap.keySet()) {
            hassioStates.addAll(hassioDeviceMap.get(entityID).getFutureStates());
        }

        Collections.sort(hassioStates);

        return hassioStates;
    }

    /**
     * Get the cached version of the future states of a single device
     * @return
     */
    public List<HassioState> getStateFuture(String id) {
        List<HassioState> hassioStates = new ArrayList<>();

        if(hassioDeviceMap.containsKey(id)) {
            hassioStates.addAll(hassioDeviceMap.get(id).getFutureStates());
        }

        Collections.sort(hassioStates);

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
     * Get the history of all device events
     * @return
     */
    public List<HassioEvent> getEventHistory() {
        List<HassioEvent> hassioStates = new ArrayList<>();

        for(String entityID : hassioDeviceMap.keySet()) {
            hassioStates.addAll(hassioDeviceMap.get(entityID).getPastEvents());
        }

        Collections.sort(hassioStates);

        return hassioStates;
    }

    /**
     * Get the cached version of all future events of each device
     * @return
     */
    public List<HassioEvent> getEventFuture() {
        List<HassioEvent> hassioStates = new ArrayList<>();

        for(String entityID : hassioDeviceMap.keySet()) {
            hassioStates.addAll(hassioDeviceMap.get(entityID).getFutureEvents());
        }

        Collections.sort(hassioStates);

        return hassioStates;
    }

    /**
     * Predict future states, based on the devices internal knowledge
     * @return
     */
    public List<HassioState> predictFutureStates() {
        List<HassioState> hassioStates = new ArrayList<>();

        for(String entityID : hassioDeviceMap.keySet()) {
            hassioStates.addAll(hassioDeviceMap.get(entityID).predictFutureStates());
        }

        Collections.sort(hassioStates);

        return hassioStates;
    }

    /**
     * Clear the cache of predicted changes
     */
    public void clearPredictions() {
        for(String entityID : hassioDeviceMap.keySet()) {
            hassioDeviceMap.get(entityID).clearPredictions();
        }
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
    public Map<String, HassioDevice> getDevices() {
        return this.hassioDeviceMap;
    }
}