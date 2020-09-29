package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.weather.HassioWeatherAttributes;
import sven.phd.iot.hassio.weather.HassioWeatherForecast;
import sven.phd.iot.scenarios.StateSet;

import java.util.Calendar;
import java.util.Date;

public class WeatherWindyStates extends StateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        HassioWeatherAttributes attributes = new HassioWeatherAttributes(16.5f);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        calendar.add(Calendar.MINUTE, 850);
        attributes.forecast.add(new HassioWeatherForecast("storm", 0.0f, 22.3f, calendar.getTime()));
        calendar.add(Calendar.MINUTE, 600);
        attributes.forecast.add(new HassioWeatherForecast("windy", 0.0f, 22.3f, calendar.getTime()));

        DM.logState(new HassioState(WeatherDevices.WEATHER_FORECAST, "cloudy", startDate, attributes));


        DM.logState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "13.5", startDate, null));
        DM.logState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "35", startDate, null));
    }

    @Override
    public void scheduleFutureStates(HassioDeviceManager DM, Calendar relativeTime) {
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "13.3", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "35", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "13.7", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "40", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "13.9", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "39", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "13.3", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "35", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "12.6", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "37", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "12.0", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "42", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "11.8", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "45", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "11.7", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "35", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "11.3", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "45", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "10.9", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "55", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "10.4", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "60", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "9.7", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "70", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "9.1", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "95", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "8.5", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "80", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "8.2", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "105", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "8.3", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "90", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "8.7", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "100", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "9.2", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "105", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "9.5", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "110", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "9.9", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "105", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "10.3", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "90", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "10.5", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "70", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "10.9", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "55", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "11.3", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "45", relativeTime.getTime(), null));
        relativeTime.add(Calendar.MINUTE, 60);
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_TEMPERATURE, "11.8", relativeTime.getTime(), null));
        DM.scheduleState(new HassioState(WeatherDevices.WEATHER_WIND_SPEED, "45", relativeTime.getTime(), null));
    }
}
