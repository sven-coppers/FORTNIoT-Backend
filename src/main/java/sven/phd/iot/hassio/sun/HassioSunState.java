package sven.phd.iot.hassio.sun;

import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;
import sven.phd.iot.hassio.sun.HassioSunAttributes;
import sven.phd.iot.hassio.weather.HassioWeatherAttributes;

import java.io.IOException;
import java.util.Date;

public class HassioSunState extends HassioState {
    public HassioSunAttributes attributes;

    public HassioSunState(HassioStateRaw hassioState) {
        super(hassioState);

        try {
            this.attributes = new ObjectMapper().readValue(hassioState.attributes.toString(), HassioSunAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(hassioState.attributes.toString());
        }
    }

    public HassioSunState(String state, Date date, float elevation, boolean rising) {
        super(new HassioContext(), "sun.sun", date, date, state);

        this.attributes = new HassioSunAttributes(elevation, rising);
    }

    @Override
    public HassioState processRawState(HassioStateRaw raw) {
        return new HassioSunState(raw);
    }
}
