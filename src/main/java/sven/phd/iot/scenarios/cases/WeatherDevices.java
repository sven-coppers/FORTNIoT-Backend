package sven.phd.iot.scenarios.cases;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.sensor.HassioSensor;
import sven.phd.iot.hassio.weather.HassioWeather;
import sven.phd.iot.scenarios.DeviceSet;

import java.util.ArrayList;

public class WeatherDevices extends DeviceSet {
    public static final String WEATHER_FORECAST         = "weather.dark_sky";
    public static final String WEATHER_WIND_SPEED       = "sensor.outdoor_wind_speed";
    public static final String WEATHER_TEMPERATURE      = "sensor.outdoor_temperature_measurement";

    @Override
    public void createDevices(ArrayList<HassioDevice> devices) {
        devices.add(new HassioWeather(WEATHER_FORECAST, "Weather description"));
        devices.add(new HassioSensor(WEATHER_WIND_SPEED, "Weather - Wind Speed (KM/H)"));
        devices.add(new HassioSensor(WEATHER_TEMPERATURE, "Weather - Temperature (°C)"));
    }
}
