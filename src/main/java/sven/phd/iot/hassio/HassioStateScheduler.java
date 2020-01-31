package sven.phd.iot.hassio;

import sven.phd.iot.hassio.states.HassioState;

import java.util.PriorityQueue;

public class HassioStateScheduler {
    private PriorityQueue<HassioState> scheduledHassioStates;
    private HassioDeviceManager hassioDeviceManager;

    public HassioStateScheduler(HassioDeviceManager hassioDeviceManager) {
        this.hassioDeviceManager = hassioDeviceManager;
        this.scheduledHassioStates = new PriorityQueue<>();
    }

    public void scheduleState(HassioState hassioState) {
        this.scheduledHassioStates.add(hassioState);
        this.reschedule();
    }

    public PriorityQueue<HassioState> getScheduledStates() {
        return this.scheduledHassioStates;
    }

    // TODO: when it is time, perform the next state

    public void performState() {
        HassioState newState = scheduledHassioStates.poll();
        this.hassioDeviceManager.stateChanged(newState);
        this.reschedule();
    }

    public void reschedule() {
        // Sleep until date-changed from first element in the queue
    }
}
