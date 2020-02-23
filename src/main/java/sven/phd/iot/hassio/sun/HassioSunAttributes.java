package sven.phd.iot.hassio.sun;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.weather.HassioWeatherForecast;

import java.util.ArrayList;
import java.util.Date;

public class HassioSunAttributes extends HassioAttributes {
    @JsonProperty("azimuth") float azimuth;
    @JsonProperty("elevation") float elevation;
    @JsonProperty("rising") boolean rising;

    @JsonProperty("next_dawn")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX")
    Date nextDawn;
    
    @JsonProperty("next_dusk")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX")
    Date nextDusk;

    @JsonProperty("next_midnight")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX")
    Date nextMidnight;

    @JsonProperty("next_noon")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX")
    Date nextNoon;

    @JsonProperty("next_rising")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX")
    Date nextRising;

    @JsonProperty("next_setting")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX")
    Date nextSetting;

    public HassioSunAttributes() {
        // Used by the serializer
    }

    public HassioSunAttributes(float elevation, boolean rising) {
        this.azimuth = 0.0f;
        this.elevation = elevation;
        this.rising = rising;
        this.nextDusk = null;
        this.nextDawn = null;
        this.nextMidnight = null;
        this.nextNoon = null;
        this.nextRising = null;
        this.nextSetting = null;
    }

    public HassioSunAttributes(JsonNode rawAttributes) {
    }
}