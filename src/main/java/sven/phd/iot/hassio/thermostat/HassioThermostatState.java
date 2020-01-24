package sven.phd.iot.hassio.thermostat;

import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;

import java.io.IOException;
import java.util.Date;

public class HassioThermostatState extends HassioState {
    public HassioThermostatAttributes attributes;

    public HassioThermostatState(HassioStateRaw hassioState) {
        super(hassioState);

        try {
            this.attributes = new ObjectMapper().readValue(hassioState.attributes.toString(), HassioThermostatAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(hassioState.attributes.toString());
        }
    }

    public HassioThermostatState(String state, float targetTemp) {
        this.attributes = new HassioThermostatAttributes();

        this.attributes.targetTemp = targetTemp;
        this.state = state;
        this.last_changed = new Date();
        this.last_updated = last_changed;
        this.datetime = last_changed;
    }
}
