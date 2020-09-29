package sven.phd.iot.hassio.bus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.api.resources.StateResource;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.*;

import java.io.IOException;
import java.util.*;

public class HassioBus extends HassioDevice {
    private HashMap<String, HassioBusPassage> uniqueBusses;

    public HassioBus(String entityID, String friendlyName) {
        super(entityID, friendlyName);

        this.uniqueBusses = new HashMap<>();
    }

    /**
     * Needed to prevent the new state from being added to the history
     * because bussed update their own history
     * @param hassioState
     */
    public void logState(HassioState hassioState) {
        // this.hassioStateHistory.add(hassioState);
        StateResource.getInstance().broadcastState(hassioState);
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        HassioBusAttributes attributes = new ObjectMapper().readValue(rawAttributes.toString(), HassioBusAttributes.class);

        attributes.correctTime(); // De lijn does not set time zone

        // Index the busses
        // Keep track of the lastest info for each bus
        for(HassioBusPassage passage : attributes.nextPassages) {
            String identifier = passage.lineNumber + passage.finalDestination + passage.scheduled;

            uniqueBusses.put(identifier, passage);
        }

        // Update the history
        this.hassioStateHistory.clear();

        for(String uniqueBus : this.uniqueBusses.keySet()) {
            HassioBusPassage passage = this.uniqueBusses.get(uniqueBus);
            HassioState busState = passageToState(passage);

            if(busState.getLastChanged().getTime() < new Date().getTime()) {
                this.hassioStateHistory.add(busState);
            }
        }

        Collections.sort(this.hassioStateHistory);

        return attributes;
    }

    @Override
    public List<HassioState> predictInitialFutureStates() {
        List<HassioState> result = new ArrayList<>();
        result.addAll(this.scheduledStates);

        for(String uniqueBus : this.uniqueBusses.keySet()) {
            HassioBusPassage passage = this.uniqueBusses.get(uniqueBus);
            HassioState busState = this.passageToState(passage);

            if(busState.getLastChanged().getTime() > new Date().getTime()) {
                result.add(busState);
            }
        }

        Collections.sort(result);

        return result;
    }

    /**
     *
     * @param passage
     * @return
     */
    public HassioState passageToState(HassioBusPassage passage) {
        HassioAttributes attributes = new HassioBusAttributes(passage);
        Date date = passage.realtime != null ? passage.realtime : passage.scheduled;
        String state = passage.lineNumberPublic + " " + passage.finalDestination;

        return new HassioState(entityID, state, date, attributes);
    }
}
