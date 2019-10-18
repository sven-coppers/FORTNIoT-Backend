package sven.phd.iot.hassio.weather;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HassioWeatherAttributes extends HassioAttributes {
    @JsonProperty("attribution") public String attribution;
    @JsonProperty("friendly_name") public String friendlyName;
    @JsonProperty("humidity") public int humidity;
    @JsonProperty("ozone") public float ozone;
    @JsonProperty("pressure") public float pressure;
    @JsonProperty("temperature") public float temperature;
    @JsonProperty("visibility") public float visibility;
    @JsonProperty("wind_bearing") public float wind_bearing;
    @JsonProperty("wind_speed") public float wind_speed;
    @JsonProperty("forecast") public List<HassioWeatherForecast> forecast;

    public HassioWeatherAttributes() {
        // Used by the serializer
    }

    public HassioWeatherAttributes(float temperature) {
        this.attribution = "Powered by Dark Sky";
        this.friendlyName = "Dark Sky";
        this.humidity = 0;
        this.ozone = 0.0f;
        this.pressure = 0.0f;
        this.temperature = temperature;
        this.visibility = 0.0f;
        this.wind_bearing = 0.0f;
        this.wind_speed = 0.0f;
        this.forecast = new ArrayList<HassioWeatherForecast>();
    }
}