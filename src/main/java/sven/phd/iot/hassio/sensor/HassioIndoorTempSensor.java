package sven.phd.iot.hassio.sensor;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.climate.HassioCooler;
import sven.phd.iot.hassio.climate.HassioHeater;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.ActionExecution;
import sven.phd.iot.rules.RuleExecution;

import java.util.*;

public class HassioIndoorTempSensor extends HassioSensor {
    private List<String> heaterIDs;
    private List<String> coolerIDs;
    private String thermostatID;
    private Map<String, HassioDevice> hassioDeviceMap;

    private double variationRate; // Degrees per hour

    public HassioIndoorTempSensor(String entityID, String friendlyName, List<String> heaterIDs, List<String> coolerIDs, String thermostatID, double variationRate) {
        super(entityID, friendlyName);
        this.heaterIDs = heaterIDs;
        this.coolerIDs = coolerIDs;
        this.thermostatID = thermostatID;

        this.variationRate = variationRate;
        this.hassioDeviceMap = ContextManager.getInstance().getHassioDeviceManager().getDevices();
    }

    @Override
    protected List<RuleExecution> predictImplicitStates(Date newDate, HashMap<String, HassioState> hassioStates) {
        List<RuleExecution> result = new ArrayList<>();

        HassioState thermostatState = hassioStates.get(this.thermostatID);
        HassioState thermometerState = hassioStates.get(this.entityID);

        Date oldDate = hassioStates.get(this.entityID).getLastChanged();
        Long deltaTimeInMilliseconds = newDate.getTime() - oldDate.getTime();
        double deltaTimeInHours = ((double) deltaTimeInMilliseconds) / (1000.0 * 60.0 * 60.0);
        double currentTemp = Double.parseDouble(thermometerState.state);
        double newTemp = currentTemp;
        boolean allEco = true;

        RuleExecution newStateEvent = new RuleExecution(newDate);
        newStateEvent.addTriggerContext(thermometerState.context);

        for(String heaterID : heaterIDs) {
            HassioState heaterState = hassioStates.get(heaterID);
            HassioHeater heater = ((HassioHeater) hassioDeviceMap.get(heaterID));

            if(heaterState != null && heater != null && heaterState.state.equals("heating")) {
                newTemp += heater.getOnRate() * deltaTimeInHours;
                newStateEvent.addConditionContext(heaterState.context);
                allEco = false;
            }
        }

        for(String coolerID : coolerIDs) {
            HassioState coolerState = hassioStates.get(coolerIDs);
            HassioCooler cooler = ((HassioCooler) hassioDeviceMap.get(coolerID));

            if(coolerState != null && coolerID != null && coolerState.state.equals("cooling")) {
                newTemp += cooler.getOnRate() * deltaTimeInHours;
                newStateEvent.addConditionContext(coolerState.context);
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
        newStateEvent.addActionExecution(new ActionExecution(this.entityID, hassioStates.get(entityID).context));
        result.add(newStateEvent);

        return result;
    }
}