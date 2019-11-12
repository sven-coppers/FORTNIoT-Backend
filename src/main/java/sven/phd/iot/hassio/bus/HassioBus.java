package sven.phd.iot.hassio.bus;

import sven.phd.iot.api.resources.UpdateResource;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;
import sven.phd.iot.hassio.updates.HassioEvent;

import java.util.*;

public class HassioBus extends HassioDevice {
    private HashMap<String, HassioBusPassage> uniqueBusses;

    public HassioBus(String entityID) {
        super(entityID);

        this.uniqueBusses = new HashMap<>();
    }

    /**
     * Needed to prevent the new state from being added to the history
     * because bussed update their own history
     * @param hassioState
     */
    protected void logState(HassioState hassioState) {
        // this.hassioStateHistory.add(hassioState);
        UpdateResource.getInstance().broadcastUpdate(hassioState);
    }

    public List<HassioContext> setState(HassioState hassioState) {
        // We cannot set the state of the weather
        return new ArrayList<>();
    }

    public HassioState processRawState(HassioStateRaw hassioStateRaw) {
        HassioBusState hassioBusState = new HassioBusState(hassioStateRaw);

        // Keep track of the lastest info for each bus
        for(HassioBusPassage passage : hassioBusState.attributes.nextPassages) {
            String identifier = passage.lineNumber + passage.finalDestination + passage.scheduled;

            uniqueBusses.put(identifier, passage);
        }

        // Update the history
        this.hassioStateHistory.clear();

        for(String uniqueBus : this.uniqueBusses.keySet()) {
            HassioBusPassage passage = this.uniqueBusses.get(uniqueBus);
            HassioBusState busState = new HassioBusState(this.entityID, uniqueBusses.get(uniqueBus));

            if(busState.last_changed.getTime() < new Date().getTime()) {
                this.hassioStateHistory.add(busState);
            }
        }

        Collections.sort(this.hassioStateHistory);

        return hassioBusState;
    }

    @Override
    public String getFriendlyName() {
        HassioBusState state = (HassioBusState) this.getLastState();
        return "Bus";
        //return state.attributes.friendly_name;
    }

    @Override
    public List<HassioState> predictFutureStates() {
        List<HassioState> result = new ArrayList<>();

        for(String uniqueBus : this.uniqueBusses.keySet()) {
            HassioBusPassage passage = this.uniqueBusses.get(uniqueBus);
            HassioBusState busState = new HassioBusState(this.entityID, uniqueBusses.get(uniqueBus));

            if(busState.last_changed.getTime() > new Date().getTime()) {
                result.add(busState);
            }
        }

        Collections.sort(result);

        return result;
    }

    @Override
    public List<HassioEvent> predictFutureEvents() {
        List<HassioEvent> result = new ArrayList<>();

        return result;
    }
}
