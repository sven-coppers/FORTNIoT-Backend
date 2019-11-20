package sven.phd.iot.hassio.tracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;

import java.io.IOException;

public class HassioDeviceTrackerState extends HassioState {
    public HassioDeviceTrackerAttributes attributes;

    public HassioDeviceTrackerState(HassioStateRaw hassioState) {
        super(hassioState);

        try {
            this.attributes = new ObjectMapper().readValue(hassioState.attributes.toString(), HassioDeviceTrackerAttributes.class);
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println(hassioState.attributes.toString());
        }
    }
}
