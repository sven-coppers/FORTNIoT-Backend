package sven.phd.iot.hassio.light;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioAttributes;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HassioLightAttributes extends HassioAttributes {
    // Always
    @JsonProperty("effect_list") public List<String> effect_list;
    @JsonProperty("max_mireds") public int max_mireds;
    @JsonProperty("min_mireds") public int min_mireds;
    @JsonProperty("supported_features") public int supported_features;

    // Sometimes
    @JsonProperty("brightness") public int brightness;
    @JsonProperty("hs_color") public List<Float> hs_color;
    @JsonProperty("rgb_color") public List<Float> rgb_color;
    @JsonProperty("xy_color") public List<Float> xy_color;
    @JsonProperty("effect") public String effect;
    @JsonProperty("flash") public String flash;
}
