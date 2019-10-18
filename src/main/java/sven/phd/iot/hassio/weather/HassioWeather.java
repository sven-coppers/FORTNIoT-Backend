package sven.phd.iot.hassio.weather;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;
import sven.phd.iot.hassio.updates.HassioEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HassioWeather extends HassioDevice {
    public HassioWeather(String entityID) {
        super(entityID);
    }

    public List<HassioContext> setState(HassioState hassioState) {
        // We cannot set the state of the weather
        return new ArrayList<HassioContext>();
    }

    public HassioState processRawState(HassioStateRaw hassioStateRaw) {
        HassioWeatherState hassioWeatherState = new HassioWeatherState(hassioStateRaw);

      //  Calendar calendar = Calendar.getInstance();
      //  calendar.setTime(hassioWeatherState.last_updated);
      //  calendar.add(Calendar.MINUTE, -10);
      //  hassioWeatherState.last_updated = calendar.getTime();

        return hassioWeatherState;
    }

    @Override
    public List<HassioState> predictFutureStates() {
        List<HassioState> result = new ArrayList<>();
        HassioWeatherState hassioWeatherState = (HassioWeatherState) this.getLastState();

        if(hassioWeatherState != null) {
            List<HassioWeatherForecast> hassioWeatherForecastList = hassioWeatherState.attributes.forecast;

            for(int i = 2; i < hassioWeatherForecastList.size(); ++i) {
                HassioWeatherForecast forecast = hassioWeatherForecastList.get(i - 1);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(forecast.datetime);
                calendar.add(Calendar.HOUR_OF_DAY, -3);
                Date previousDate = calendar.getTime();

                result.add(new HassioWeatherState(forecast.condition, previousDate, forecast.precipitation, forecast.temperature));
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
