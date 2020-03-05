package sven.phd.iot.hassio.sensor;

import sven.phd.iot.hassio.climate.HassioCoolerAttributes;
import sven.phd.iot.hassio.climate.HassioHeaterAttributes;
import sven.phd.iot.hassio.climate.HassioThermostatAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.ImplicitBehaviorEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HassioIndoorTempSensor extends HassioSensor {
    private List<String> heaterIDs;
    private List<String> coolerIDs;
    private String thermostatID;

    private double variationRate = -0.5; // Degrees per hour

    public HassioIndoorTempSensor(String entityID, String friendlyName, List<String> heaterIDs, List<String> coolerIDs, String thermostatID, double variationRate) {
        super(entityID, friendlyName);
        this.heaterIDs = heaterIDs;
        this.coolerIDs = coolerIDs;
        this.thermostatID = thermostatID;

        this.variationRate = variationRate;
    }

    @Override
    protected List<ImplicitBehaviorEvent> predictImplicitStates(Date newDate, HashMap<String, HassioState> hassioStates) {
        List<ImplicitBehaviorEvent> result = new ArrayList<>();

        HassioState thermostatState = hassioStates.get(this.thermostatID);

        if(thermostatState == null) return result;

        double targetTemp = Double.parseDouble(thermostatState.state);
        Date oldDate = hassioStates.get(this.entityID).getLastChanged();
        Long deltaTimeInMilliseconds = newDate.getTime() - oldDate.getTime();
        double deltaTimeInHours = ((double) deltaTimeInMilliseconds) / (1000.0 * 60.0 * 60.0);
        double currentTemp = Double.parseDouble((hassioStates.get(this.entityID).state));
        double newTemp = currentTemp;
        boolean allEco = true;

        ImplicitBehaviorEvent newStateEvent = new ImplicitBehaviorEvent(newDate);

        for(String heaterID : heaterIDs) {
            HassioState heaterState = hassioStates.get(heaterID);

            if(heaterState != null && heaterState.state.equals("heating")) {
                newTemp += ((HassioHeaterAttributes) heaterState.attributes).heatingRate * deltaTimeInHours;
                newStateEvent.addTriggerDeviceID(heaterID);
                allEco = false;
            }
        }

        for(String coolerID : coolerIDs) {
            HassioState coolerState = hassioStates.get(coolerIDs);

            if(coolerState != null && coolerState.state.equals("cooling")) {
                newTemp += ((HassioCoolerAttributes) coolerState.attributes).coolingRate * deltaTimeInHours;
                newStateEvent.addTriggerDeviceID(coolerID);
                allEco = false;
            }
        }

        if(allEco && targetTemp < currentTemp) {
            newTemp = Math.max(newTemp + variationRate * deltaTimeInHours, targetTemp);
        } else {
            //newTemp += variationRate * deltaTimeInHours;
        }

        // Give the new state the old date, because it might be changed by another device as well
        hassioStates.put(this.entityID, new HassioState(this.entityID, "" + newTemp, oldDate, new HassioSensorAttributes("temperature", "°C")));
        newStateEvent.addActionDeviceID(this.entityID);
        newStateEvent.addTriggerDeviceID(this.entityID);
        result.add(newStateEvent);

        return result;
    }
}