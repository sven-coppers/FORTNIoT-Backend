package sven.phd.iot.rules.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.light.HassioLightAttributes;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Action;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class LightOnAction extends Action {
    @JsonProperty("deviceID") public String deviceIdentifier;

    @JsonDeserialize(using = ColorDeserializer.class)
    @JsonSerialize(using = ColorSerializer.class)
    @JsonProperty("color") public Color color;

    @JsonProperty("flash") public boolean flash;

    // For deserialization
    public LightOnAction() {
        enabled = true;
    }

    public LightOnAction(String description, String deviceIdentifier, Color color, boolean flash) {
        // super("Turn on " + friendlyName + " {color: [" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "], flash: " + flash + "}");
        super(description);
        this.deviceIdentifier = deviceIdentifier;

        if(color == null) {
            color = Color.YELLOW;
        }

        this.color = color;
        this.flash = flash;
    }

    public List<HassioState> simulate(Date datetime, HashMap<String, HassioState> hassioStates) {
        List<HassioState> newStates = new ArrayList<>();

        if (!isEnabled(datetime)) {
            return newStates;
        }

        HassioLightAttributes attributes = new HassioLightAttributes();
        attributes.brightness = 254;

        if(this.flash) {
            attributes.flash = "short";
        }

        List<Float> rgb_colors = new ArrayList<>();
        rgb_colors.add((float) this.color.getRed() / 255.0f);
        rgb_colors.add((float) this.color.getGreen() / 255.0f);
        rgb_colors.add((float) this.color.getBlue() / 255.0f);

        attributes.rgb_color = rgb_colors;

        newStates.add(new HassioState(deviceIdentifier, "on", datetime, attributes));

        return newStates;
    }

    public String getDeviceID() { return this.deviceIdentifier; }


    @Override
    public boolean onSameDevice(Action other) {
        if (this == other) {
            return true;
        }
        if (other == null || (this.getClass() != other.getClass() && LightOffAction.class != other.getClass())) {
            return false;
        }
        if (this.getClass() == other.getClass()) {
            return this.deviceIdentifier.equals(((LightOnAction) other).deviceIdentifier);
        }
        if (LightOffAction.class == other.getClass()) {
            return this.deviceIdentifier.equals(((LightOffAction) other).getDeviceID());
        }

        return false;
    }

    @Override
    public boolean isSimilar(Action other) {
        if (this == other) {
            return true;
        }
        if (other == null || (this.getClass() != other.getClass() && LightOffAction.class != other.getClass())) {
            return false;
        }
        if (this.getClass() == other.getClass()) {
            boolean result = true;
            if (!this.deviceIdentifier.equals(((LightOnAction) other).deviceIdentifier)) {
                result = false;
            }
            int rgb = this.color.getRGB();
            if (this.color.getRGB() != ((LightOnAction) other).color.getRGB()) {
                result = false;
            }
            return result;
        }

        return false;
    }
}