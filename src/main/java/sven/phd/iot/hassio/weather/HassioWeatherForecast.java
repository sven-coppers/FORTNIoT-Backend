package sven.phd.iot.hassio.weather;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class HassioWeatherForecast {
    @JsonProperty("condition") String condition;
    @JsonProperty("precipitation") float precipitation;
    @JsonProperty("temperature") float temperature;

    @JsonProperty("datetime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    Date datetime;
}
