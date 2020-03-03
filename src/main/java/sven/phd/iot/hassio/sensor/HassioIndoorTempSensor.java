package sven.phd.iot.hassio.sensor;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.ImplicitBehaviorEvent;
import sven.phd.iot.rules.RulesManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HassioIndoorTempSensor extends HassioSensor {
    private double coolingRate = -0.5; // Degrees per hour

    public HassioIndoorTempSensor(String entityID, String friendlyName, double coolingRate) {
        super(entityID, friendlyName);

        this.coolingRate = coolingRate;
    }

    @Override
    protected List<ImplicitBehaviorEvent> predictFutureStatesUsingContext(Date newDate, HashMap<String, HassioState> hassioStates) {
        List<ImplicitBehaviorEvent> result = new ArrayList<>();

        Date oldDate = hassioStates.get(this.entityID).getLastChanged();
        Long deltaTimeInMilliseconds = newDate.getTime() - oldDate.getTime();
        double deltaTimeInHours = ((double) deltaTimeInMilliseconds) / (1000.0 * 60.0 * 60.0);
        double currentTemp = Double.parseDouble((hassioStates.get(this.entityID).state));
        double newTemp = currentTemp + coolingRate * deltaTimeInHours;

        // Give the new state the old date, because it might be changed by another device as well
        hassioStates.put(this.entityID, new HassioState(this.entityID, "" + newTemp, oldDate, new HassioSensorAttributes("temperature", "°C")));

        ImplicitBehaviorEvent newStateEvent = new ImplicitBehaviorEvent(newDate);
        newStateEvent.addActionDeviceID(this.entityID);
        newStateEvent.addTriggerDeviceID(this.entityID);
        result.add(newStateEvent);

        return result;
    }
}