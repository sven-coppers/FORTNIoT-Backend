package sven.phd.iot.hassio.thermostat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HassioThermostat extends HassioDevice {
    public HassioThermostat(String entityID, String friendlyName) {
        super(entityID, friendlyName);
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioThermostatAttributes.class);
    }

    public List<HassioContext> setState(HassioState hassioState) {
        // For now only a virtual device
        return new ArrayList<>();
    }

    @Override
    public List<HassioState> getFutureStates() {
        // A lamp cannot know its future state
        return new ArrayList<HassioState>();
    }

    @Override
    public List<HassioEvent> predictFutureEvents() {
        // A lamp cannot know its future events
        return new ArrayList<HassioEvent>();
    }

    @Override
    protected HassioState adaptStateToContext(Date newDate, HashMap<String, HassioState> hassioStates) {
        HassioState thermostatState = hassioStates.get(this.entityID);
        HassioState indoorTempState = hassioStates.get("sensor.indoor_temperature_measurement");

        if(thermostatState == null || indoorTempState == null) return null;

        double targetTemp = ((HassioThermostatAttributes) thermostatState.attributes).targetTemp;
        double currentTemp = Double.parseDouble(indoorTempState.state);

        if(thermostatState.state.equals("eco")) {
            // Do nothing
        } else if(thermostatState.state.equals("heating") && currentTemp > targetTemp) {
            return new HassioState(thermostatState.entity_id, "eco", newDate, thermostatState.attributes);
        } else if(thermostatState.state.equals("cooling") && currentTemp < targetTemp) {
            return new HassioState(thermostatState.entity_id, "eco", newDate, thermostatState.attributes);
        } else if(thermostatState.state.equals("eco") && currentTemp < targetTemp) {
            return new HassioState(thermostatState.entity_id, "heating", newDate, thermostatState.attributes);
        }

        return null;



      /*  if(heaterState != null && heaterState.state.equals("eco")) {
            double targetTemp = ((HassioThermostatAttributes) heaterState.attributes).targetTemp;
        }




            double deltaTemp = 0.0;

            if (currentTemp > targetTemp) {
                deltaTemp = coolingRate * deltaTimeInHours;

                if(currentTemp - deltaTemp < targetTemp) {
                    deltaTemp = 0.0;
                }
            } else if (currentTemp < targetTemp) {
                deltaTemp = heatingRate * deltaTimeInHours;

                if(currentTemp + deltaTemp > targetTemp) {
                    deltaTemp = 0.0;
                }
            } */



    }
}
