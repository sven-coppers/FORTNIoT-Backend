package sven.phd.iot.hassio.weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioAbstractState;
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

    public List<HassioContext> setState(HassioState hassioState) {
        // We cannot set the state of the weather
        return new ArrayList<HassioContext>();
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioWeatherAttributes.class);
    }

    @Override
    public List<HassioState> predictFutureStates() {
        List<HassioState> result = new ArrayList<>();
        HassioState state = this.getLastState();

        if(state != null) {
            HassioWeatherAttributes attributes = (HassioWeatherAttributes) state.attributes;
            List<HassioWeatherForecast> hassioWeatherForecastList = attributes.forecast;

            for(int i = 2; i < hassioWeatherForecastList.size(); ++i) {
                HassioWeatherForecast forecast = hassioWeatherForecastList.get(i - 1);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(forecast.datetime);
                calendar.add(Calendar.HOUR_OF_DAY, -3);
                Date previousDate = calendar.getTime();

                new HassioState(this.entityID, forecast.condition, previousDate, new HassioWeatherAttributes(forecast.temperature));
            }
        }

        return result;
    }

    @Override
    public List<HassioEvent> predictFutureEvents() {
        List<HassioEvent> result = new ArrayList<HassioEvent>();

        return result;
    }
}
