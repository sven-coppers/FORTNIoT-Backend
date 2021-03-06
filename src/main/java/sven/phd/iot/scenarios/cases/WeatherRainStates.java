package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.weather.HassioWeatherAttributes;
import sven.phd.iot.hassio.weather.HassioWeatherForecast;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class WeatherRainStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        HassioWeatherAttributes attributes = new HassioWeatherAttributes(16.5f);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        calendar.add(Calendar.MINUTE, 250);
        attributes.forecast.add(new HassioWeatherForecast("light_rain", 0.0f, 0.0f, calendar.getTime()));
        calendar.add(Calendar.MINUTE, 30);
        attributes.forecast.add(new HassioWeatherForecast("rainy", 0.0f, 16.0f, calendar.getTime()));
        calendar.add(Calendar.MINUTE, 250);
        attributes.forecast.add(new HassioWeatherForecast("rain_clouds", 0.0f, 13.3f, calendar.getTime()));
        calendar.add(Calendar.MINUTE, 80);
        attributes.forecast.add(new HassioWeatherForecast("partlycloudy", 0.0f, 16.0f, calendar.getTime()));

        DM.logState(new HassioState(WeatherDevices.WEATHER_FORECAST, "cloudy", startDate, attributes));
    }

    @Override
    public void scheduleFutureStates(HassioDeviceManager DM, Calendar relativeTime) {

    }
}
