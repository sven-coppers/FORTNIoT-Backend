package sven.phd.iot.hassio.light;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioState;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL) // Only include fields that are not NULL
public class HassioLightServiceOn extends HassioLightService {
    @JsonProperty("profile") public String profile;
    @JsonProperty("hs_color") public List<Float> hs_color;
    @JsonProperty("rgb_color") public List<Float> rgb_color;
    @JsonProperty("xy_color") public List<Float> xy_color;
    //@JsonProperty("white_value") public int white_value;
    //@JsonProperty("color_temp") public int color_temp;
    //@JsonProperty("kelvin") public int kelvin;
    //@JsonProperty("color_name") public String color_name;
    @JsonProperty("brightness") public int brightness;
    @JsonProperty("flash") public String flash;
    @JsonProperty("effect") public String effect;

    public HassioLightServiceOn(HassioState lightState) {
        HassioLightAttributes attributes = (HassioLightAttributes) lightState.attributes;

        this.entity_id = lightState.entity_id;
        this.transition = true;
        this.hs_color = attributes.hs_color;
        this.rgb_color = attributes.rgb_color;
        this.xy_color = attributes.xy_color;
        //this.white_value = 0;
        this.brightness = attributes.brightness;
        this.effect = attributes.effect;
        this.flash = attributes.flash;
    }
}