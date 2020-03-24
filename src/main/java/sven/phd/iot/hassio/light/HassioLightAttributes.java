package sven.phd.iot.hassio.light;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioAttributes;
//import sven.phd.iot.hassio.states.HassioConflictingAttribute;

import java.util.ArrayList;
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

    /**
     * Override equals function
     * @param other
     * @return List of attributes that aren't equal
     *//*
    @Override
    public List<HassioConflictingAttribute> checkForConflicts(HassioLightAttributes other) {
        List<HassioConflictingAttribute> result = super.checkForConflicts(other);

        if (this.effect_list == null || other.effect_list == null ) {
            if (this.effect_list == null && other.effect_list != null) {
                result.add(new HassioConflictingAttribute("effect_list", "null", other.effect_list.toString()));
            } else if (this.effect_list != null && other.effect_list == null) {
                result.add(new HassioConflictingAttribute("effect_list", this.effect_list.toString(), "null"));
            }
        } else if (!this.effect_list.equals(other.effect_list)) {
            result.add(new HassioConflictingAttribute("effect_list", this.effect_list.toString(), other.effect_list.toString()));
        }
        if (this.max_mireds != other.max_mireds) {
            result.add(new HassioConflictingAttribute("max_mireds", Integer.toString(this.max_mireds), Integer.toString(other.max_mireds)));
        }
        if (this.min_mireds != other.min_mireds) {
            result.add(new HassioConflictingAttribute("min_mireds", Integer.toString(this.min_mireds), Integer.toString(other.min_mireds)));
        }
        if (this.supported_features != other.supported_features) {
            result.add(new HassioConflictingAttribute("supported_features", Integer.toString(this.supported_features), Integer.toString(other.supported_features)));
        }
        if (this.brightness != other.brightness) {
            result.add(new HassioConflictingAttribute("brightness", Integer.toString(this.brightness), Integer.toString(other.brightness)));
        }
        if (this.hs_color == null || other.hs_color == null ) {
            if (this.hs_color == null && other.hs_color != null) {
                result.add(new HassioConflictingAttribute("hs_color", "null", other.hs_color.toString()));
            } else if (this.hs_color != null && other.hs_color == null) {
                result.add(new HassioConflictingAttribute("hs_color", this.hs_color.toString(), "null"));
            }
        } else if (!this.hs_color.equals(other.hs_color)) {
            result.add(new HassioConflictingAttribute("hs_color", this.hs_color.toString(), other.hs_color.toString()));
        }
        if (this.rgb_color == null || other.rgb_color == null ) {
            if (this.rgb_color == null && other.rgb_color != null) {
                result.add(new HassioConflictingAttribute("rgb_color", "null", other.rgb_color.toString()));
            } else if (this.rgb_color != null && other.rgb_color == null) {
                result.add(new HassioConflictingAttribute("rgb_color", this.rgb_color.toString(), "null"));
            }
        } else if (!this.rgb_color.equals(other.rgb_color)) {
            result.add(new HassioConflictingAttribute("rgb_color", this.rgb_color.toString(), other.rgb_color.toString()));
        }
        if (this.xy_color == null || other.xy_color == null ) {
            if (this.xy_color == null && other.xy_color != null) {
                result.add(new HassioConflictingAttribute("xy_color", "null", other.xy_color.toString()));
            } else if (this.xy_color != null && other.xy_color == null) {
                result.add(new HassioConflictingAttribute("xy_color", this.xy_color.toString(), "null"));
            }
        } else if (!this.xy_color.equals(other.xy_color)) {
            result.add(new HassioConflictingAttribute("xy_color", this.xy_color.toString(), other.xy_color.toString()));
        }
        if (this.effect != other.effect) {
            result.add(new HassioConflictingAttribute("effect", this.effect, other.effect));
        }
        if (this.flash != other.flash) {
            result.add(new HassioConflictingAttribute("flash", this.flash, other.flash));
        }

        return result;
    }*/
}
