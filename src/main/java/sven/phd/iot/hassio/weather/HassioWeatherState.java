package sven.phd.iot.hassio.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioAbstractState;
import sven.phd.iot.hassio.states.HassioStateRaw;

import java.io.IOException;
import java.util.Date;

public class HassioWeatherState extends HassioAbstractState {
    public HassioWeatherAttributes attributes;

    public HassioWeatherState(String condition, Date date, float precipitation, float temperature) {
        super(new HassioContext("weather.dark_sky", date), "weather.dark_sky", date, date, condition);

        this.attributes = new HassioWeatherAttributes(temperature);
    }

    public HassioWeatherState(HassioStateRaw hassioState) {
        super(hassioState);

        try {
            this.attributes = new ObjectMapper().readValue(hassioState.attributes.toString(), HassioWeatherAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(hassioState.attributes.toString());
        }
    }
}
