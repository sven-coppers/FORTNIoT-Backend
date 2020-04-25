package sven.phd.iot.hassio.cleaning;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.sensor.HassioBattery;
import sven.phd.iot.hassio.sensor.HassioBatteryAttributes;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    public List<HassioContext> setState(HassioState hassioState) {
        return new ArrayList<>();
    }

    /**
     * Let the device predict its own future state, based on the last state of all devices in the previous frame (e.g. update temperature)...
     * This function is called ONCE at the beginning of every 'frame' in the simulation
     * @return
     */
    protected List<ImplicitBehaviorEvent> predictImplicitStates(Date newDate, HashMap<String, HassioState> hassioStates) {
        ArrayList<ImplicitBehaviorEvent> result = new ArrayList<>();

        HassioState roombaState = hassioStates.get(this.entityID);
        HassioState batteryState = hassioStates.get(this.batteryID);

        if(batteryState == null || roombaState == null) return result;

        double oldBatteryState = Double.parseDouble(batteryState.state);
        Long deltaTimeInMilliseconds = newDate.getTime() - batteryState.getLastChanged().getTime();
        double deltaTimeInMinutes = ((double) deltaTimeInMilliseconds) / (1000.0 * 60.0);
        double newBatteryState = oldBatteryState;
        ImplicitBehaviorEvent event = new ImplicitBehaviorEvent(newDate);
        event.addActionDeviceID(this.batteryID);

        if(roombaState.state.equals("cleaning")) {
            newBatteryState -= (deltaTimeInMinutes / depletionMinutesPerProcent);
            event.addTriggerDeviceID(this.entityID);
        } else if(roombaState.state.equals("docked")) {
            newBatteryState = Math.min(100.0, newBatteryState + (deltaTimeInMinutes / chargingMinutesPerProcent));

            event.addTriggerDeviceID(this.batteryID);
            if(newBatteryState != 100.0) {
                event.addTriggerDeviceID(this.entityID);
            }
        }

        hassioStates.put(this.batteryID, new HassioState(this.batteryID, "" + newBatteryState, batteryState.getLastChanged(), null));

        result.add(event);
        return result;
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioCleanerAttributes.class);
    }
}
