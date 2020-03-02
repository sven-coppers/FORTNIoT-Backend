package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.tv.HassioTV;
import sven.phd.iot.hassio.tv.HassioTVGuide;
import sven.phd.iot.hassio.weather.HassioWeather;
import sven.phd.iot.study.StudyDeviceSet;

import java.util.ArrayList;

public class WeatherDevices extends StudyDeviceSet {
    public static final String WEATHER_FORECAST = "weather.dark_sky";

    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        devices.add(new HassioWeather(WEATHER_FORECAST, "Weather"));
    }
}
