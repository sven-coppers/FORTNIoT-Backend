package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.weather.HassioWeatherAttributes;
import sven.phd.iot.hassio.weather.HassioWeatherForecast;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class WeatherClearStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        HassioWeatherAttributes attributes = new HassioWeatherAttributes(16.5f);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        calendar.add(Calendar.MINUTE, 200);
        attributes.forecast.add(new HassioWeatherForecast("partlycloudy", 0.0f, 22.3f, calendar.getTime()));
        calendar.add(Calendar.MINUTE, 30);
        attributes.forecast.add(new HassioWeatherForecast("sunny", 0.0f, 23.0f, calendar.getTime()));
        calendar.add(Calendar.MINUTE, 250);
        attributes.forecast.add(new HassioWeatherForecast("clear-night", 0.0f, 13.3f, calendar.getTime()));
        calendar.add(Calendar.MINUTE, 180);
        attributes.forecast.add(new HassioWeatherForecast("cloudynight", 0.0f, 16.0f, calendar.getTime()));
        calendar.add(Calendar.MINUTE, 120);
        attributes.forecast.add(new HassioWeatherForecast("cloudynight", 0.0f, 16.0f, calendar.getTime()));
        calendar.add(Calendar.MINUTE, 180);
        attributes.forecast.add(new HassioWeatherForecast("sunny", 0.0f, 21.0f, calendar.getTime()));

        DM.logState(new HassioState(WeatherDevices.WEATHER_FORECAST, "sunny", startDate, attributes));
        DM.logState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "22.3", startDate, null));
        DM.logState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "35", startDate, null));
    }

    @Override
    public void scheduleFutureStates(HassioDeviceManager DM, Calendar relativeTime) {
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "22.5", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "36", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "21.2", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "37", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "20.8", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "38", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "20.1", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "38", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "19.6", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "38", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "18.3", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "38", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "16.9", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "38", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "15.3", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "37", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "14.7", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "36", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "14.6", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "36", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "14.8", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "36", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "15.1", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "36", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "16.1", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "40", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "16.7", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "44", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "17.4", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "39", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "18.7", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "34", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "20.1", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "35", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "21.6", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "36", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "22.5", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "37", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "23.4", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "40", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "24.5", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "45", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "25.5", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "40", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "26.3", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "39", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "25.7", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "38", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "25.2", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "37", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);

    }
}
