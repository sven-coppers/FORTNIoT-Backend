package sven.phd.iot.hassio.sensor;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.climate.HassioCooler;
import sven.phd.iot.hassio.climate.HassioCoolerAttributes;
import sven.phd.iot.hassio.climate.HassioHeater;
import sven.phd.iot.hassio.climate.HassioHeaterAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.ImplicitBehaviorEvent;

import java.util.*;

public class HassioIndoorTempSensor extends HassioSensor {
    private List<String> heaterIDs;
    private List<String> coolerIDs;
    private String thermostatID;

    private double variationRate; // Degrees per hour

    public HassioIndoorTempSensor(String entityID, String friendlyName, List<String> heaterIDs, List<String> coolerIDs, String thermostatID, double variationRate) {
        super(entityID, friendlyName);
        this.heaterIDs = heaterIDs;
        this.coolerIDs = coolerIDs;
        this.thermostatID = thermostatID;

        this.variationRate = variationRate;
    }

    @Override
    protected List<ImplicitBehaviorEvent> predictImplicitStates(Date newDate, HashMap<String, HassioState> hassioStates, Map<String, HassioDevice> hassioDeviceMap) {
        List<ImplicitBehaviorEvent> result = new ArrayList<>();

        HassioState thermostatState = hassioStates.get(this.thermostatID);

        Date oldDate = hassioStates.get(this.entityID).getLastChanged();
        Long deltaTimeInMilliseconds = newDate.getTime() - oldDate.getTime();
        double deltaTimeInHours = ((double) deltaTimeInMilliseconds) / (1000.0 * 60.0 * 60.0);
        double currentTemp = Double.parseDouble((hassioStates.get(this.entityID).state));
        double newTemp = currentTemp;
        boolean allEco = true;

        ImplicitBehaviorEvent newStateEvent = new ImplicitBehaviorEvent(newDate);

        for(String heaterID : heaterIDs) {
            HassioState heaterState = hassioStates.get(heaterID);
            HassioHeater heater = ((HassioHeater) hassioDeviceMap.get(heaterID));

            if(heaterState != null && heater != null && heaterState.state.equals("heating")) {

                newTemp += heater.getOnRate() * deltaTimeInHours;
                newStateEvent.addTriggerDeviceID(heaterID);
                allEco = false;
            }
        }

        for(String coolerID : coolerIDs) {
            HassioState coolerState = hassioStates.get(coolerIDs);
            HassioCooler cooler = ((HassioCooler) hassioDeviceMap.get(coolerID));

            if(coolerState != null && coolerID != null && coolerState.state.equals("cooling")) {
                newTemp += cooler.getOnRate() * deltaTimeInHours;
                newStateEvent.addTriggerDeviceID(coolerID);
                allEco = false;
            }
        }

        if(thermostatState == null) {
            newTemp = newTemp + variationRate * deltaTimeInHours;
        } else {
            double targetTemp = Double.parseDouble(thermostatState.state);

            if(allEco && targetTemp < currentTemp && variationRate < 0.0) {
                newTemp = Math.max(newTemp + variationRate * deltaTimeInHours, targetTemp);
            } else if(allEco && targetTemp > currentTemp && variationRate > 0.0) {
                newTemp = Math.min(newTemp + variationRate * deltaTimeInHours, targetTemp);
            } else {
                //newTemp += variationRate * deltaTimeInHours;
            }
        }

        // Give the new state the old date, because it might be changed by another device as well
        hassioStates.put(this.entityID, new HassioState(this.entityID, "" + newTemp, oldDate, new HassioSensorAttributes("temperature", "°C")));
        newStateEvent.addActionDeviceID(this.entityID);
        newStateEvent.addTriggerDeviceID(this.entityID);
        result.add(newStateEvent);

        return result;
    }
}