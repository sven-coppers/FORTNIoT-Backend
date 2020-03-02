package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.weather.HassioWeatherAttributes;
import sven.phd.iot.hassio.weather.HassioWeatherForecast;
import sven.phd.iot.study.StudyStateSet;

import java.util.Calendar;
import java.util.Date;

public class WeatherWindyStates extends StudyStateSet {
    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        HassioWeatherAttributes attributes = new HassioWeatherAttributes(16.5f);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        calendar.add(Calendar.MINUTE, 120);
        attributes.forecast.add(new HassioWeatherForecast("windy", 0.0f, 22.3f, calendar.getTime()));
        calendar.add(Calendar.MINUTE, 200);
        attributes.forecast.add(new HassioWeatherForecast("storm", 0.0f, 23.0f, calendar.getTime()));
        calendar.add(Calendar.MINUTE, 45);
        attributes.forecast.add(new HassioWeatherForecast("windy", 0.0f, 13.3f, calendar.getTime()));
        calendar.add(Calendar.MINUTE, 120);
        attributes.forecast.add(new HassioWeatherForecast("partlycloudy", 0.0f, 16.0f, calendar.getTime()));

        DM.logState(new HassioState(WeatherDevices.WEATHER_FORECAST, "cloudy", startDate, attributes));
    }

    @Override
    public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime) {

    }
}
