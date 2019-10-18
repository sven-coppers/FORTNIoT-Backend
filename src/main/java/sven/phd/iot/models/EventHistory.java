package sven.phd.iot.models;

import sven.phd.iot.api.resources.EventResource;

import java.util.ArrayList;
import java.util.List;

public class EventHistory {
    private static EventHistory eventHistory;

    public List<Event> history;

    private EventHistory() {
        System.out.println("Initiating event history...");
        this.history = new ArrayList<Event>();
    }

    public static EventHistory getInstance() {
        if(eventHistory == null) {
            eventHistory = new EventHistory();
        }

        return eventHistory;
    }

    public List<Event> getHistory() {
        return history;
    }

    public void addEvent(Event event) {
        this.history.add(event);
        EventResource.getInstance().broadcastEvent(event);
    }
}