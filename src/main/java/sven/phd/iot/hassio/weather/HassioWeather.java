package sven.phd.iot.hassio.weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HassioWeather extends HassioDevice {
    public HassioWeather(String entityID, String friendlyName) {
        super(entityID, friendlyName);
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioWeatherAttributes.class);
    }

    @Override
    public List<HassioState> getFutureStates() {
        List<HassioState> result = new ArrayList<>();
        HassioState state = this.getLastState();

        if(state != null) {
            HassioWeatherAttributes attributes = (HassioWeatherAttributes) state.attributes;
            List<HassioWeatherForecast> hassioWeatherForecastList = attributes.forecast;

            for(int i = 0; i < hassioWeatherForecastList.size(); ++i) {
                HassioWeatherForecast forecast = hassioWeatherForecastList.get(i);

                if(forecast.datetime.getTime() > new Date().getTime()) {
                    result.add(new HassioState(this.entityID, forecast.condition, forecast.datetime, new HassioWeatherAttributes(forecast.temperature)));
                }
            }
        }

        return result;
    }
}
