package sven.phd.iot.hassio.cleaning;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.sensor.HassioBatteryAttributes;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.Future;
import sven.phd.iot.rules.ActionExecution;
import sven.phd.iot.rules.RuleExecution;

import java.io.IOException;
import java.util.*;

public class HassioCleaner extends HassioDevice {
    private String batteryID;
    private final int depletionMinutesPerPercent;
    private final int chargingMinutesPerPercent;

    public HassioCleaner(String entityID, String friendlyName, String batteryID, int depletionMinutesPerPercent, int chargingMinutesPerPercent) {
        super(entityID, friendlyName);
        this.batteryID = batteryID;
        this.depletionMinutesPerPercent = depletionMinutesPerPercent;
        this.chargingMinutesPerPercent = chargingMinutesPerPercent;
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioCleanerAttributes.class);
    }

    /**
     * Let the device predict its own future state, based on the last state of all devices in the previous frame (e.g. update temperature)...
     * This function is called ONCE at the beginning of every 'frame' in the simulation
     * @return
     */
    protected List<HassioState> predictTickFutureStates(Date newDate, Future future) {
        ArrayList<HassioState> newStates = new ArrayList<>();
        HashMap<String, HassioState> hassioStates = future.getLastStates();

        HassioState roombaState = hassioStates.get(this.entityID);
        HassioState batteryState = hassioStates.get(this.batteryID);

        if(batteryState == null || roombaState == null) return newStates;

        double oldBatteryValue = Double.parseDouble(batteryState.state);
        Long deltaTimeInMilliseconds = newDate.getTime() - batteryState.getLastChanged().getTime();
        double deltaTimeInMinutes = ((double) deltaTimeInMilliseconds) / (1000.0 * 60.0);
        double newBatteryValue = oldBatteryValue;
        RuleExecution event = new RuleExecution(newDate, this.entityID + "_update_battery", batteryState.context);

        if(roombaState.state.equals("cleaning")) {
            newBatteryValue -= (deltaTimeInMinutes / depletionMinutesPerPercent);
            event.addConditionContext(roombaState.context);
        } else if(roombaState.state.equals("docked")) {
            newBatteryValue = Math.min(100.0, newBatteryValue + (deltaTimeInMinutes / chargingMinutesPerPercent));
            event.addConditionContext(batteryState.context);

            if(newBatteryValue != 100.0) {
                event.addConditionContext(roombaState.context);
            }
        }

        // Add state to return list
        HassioState newBatteryState = new HassioState(this.batteryID, "" + newBatteryValue, batteryState.getLastChanged(), new HassioBatteryAttributes());
        newStates.add(newBatteryState);

        // Add event to future
        event.addActionExecution(new ActionExecution("battery_update", hassioStates.get(this.batteryID).context));
        future.addExecutionEvent(event);

        return newStates;
    }
}