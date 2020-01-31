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

    public HassioThermostatState(String entityID, String state, float targetTemp, Date date) {
        this.attributes = new HassioThermostatAttributes();
        this.entity_id =entityID;

        this.attributes.targetTemp = targetTemp;
        this.state = state;
        this.last_changed = date;
        this.last_updated = date;
        this.datetime = date;
    }

    @Override
    public HassioState processRawState(HassioStateRaw raw) {
        System.err.println("processRawState not supported HassioThermostat");
        return null;
    }
}
