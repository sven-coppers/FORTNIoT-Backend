package sven.phd.iot.hassio.cleaning;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.ActionExecution;
import sven.phd.iot.rules.RuleExecution;

import java.io.IOException;
import java.util.*;

public class HassioCleaner extends HassioDevice {
    private String batteryID;
    private int depletionMinutesPerProcent;
    private int chargingMinutesPerProcent;

    public HassioCleaner(String entityID, String friendlyName, String batteryID, int depletionMinutesPerProcent, int chargingMinutesPerProcent) {
        super(entityID, friendlyName);
        this.batteryID = batteryID;
        this.depletionMinutesPerProcent = depletionMinutesPerProcent;
        this.chargingMinutesPerProcent = chargingMinutesPerProcent;
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
    protected List<RuleExecution> predictImplicitStates(Date newDate, HashMap<String, HassioState> hassioStates) {
        ArrayList<RuleExecution> result = new ArrayList<>();

        HassioState roombaState = hassioStates.get(this.entityID);
        HassioState batteryState = hassioStates.get(this.batteryID);

        if(batteryState == null || roombaState == null) return result;

        double oldBatteryState = Double.parseDouble(batteryState.state);
        Long deltaTimeInMilliseconds = newDate.getTime() - batteryState.getLastChanged().getTime();
        double deltaTimeInMinutes = ((double) deltaTimeInMilliseconds) / (1000.0 * 60.0);
        double newBatteryState = oldBatteryState;
        RuleExecution event = new RuleExecution(newDate);

        if(roombaState.state.equals("cleaning")) {
            newBatteryState -= (deltaTimeInMinutes / depletionMinutesPerProcent);
            event.addTriggerContext(roombaState.context);
        } else if(roombaState.state.equals("docked")) {
            newBatteryState = Math.min(100.0, newBatteryState + (deltaTimeInMinutes / chargingMinutesPerProcent));

            event.addTriggerContext(batteryState.context);
            if(newBatteryState != 100.0) {
                event.addTriggerContext(roombaState.context);
            }
        }

        hassioStates.put(this.batteryID, new HassioState(this.batteryID, "" + newBatteryState, batteryState.getLastChanged(), null));
        event.addActionExecution(new ActionExecution("battery_update", hassioStates.get(this.batteryID).context));

        result.add(event);
        return result;
    }
}