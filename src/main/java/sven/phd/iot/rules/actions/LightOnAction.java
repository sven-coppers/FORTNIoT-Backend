package sven.phd.iot.rules.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.light.HassioLightState;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.Action;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LightOnAction extends Action {
    @JsonProperty("entity_id")
    private String deviceIdentifier;
    private Color color;
    private boolean flash;

    public LightOnAction(String deviceIdentifier, Color color, boolean flash) {
        super("Turn on " + deviceIdentifier + " {color: [" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "], flash: " + flash + "}");
        this.deviceIdentifier = deviceIdentifier;

        if(color == null) {
            color = Color.YELLOW;
        }

        this.color = color;
        this.flash = flash;
    }

    public List<HassioState> simulate(HassioRuleExecutionEvent hassioRuleExecutionEvent) {
        List<HassioState> newStates = new ArrayList<>();

        HassioLightState hassioLightState = new HassioLightState(deviceIdentifier, "on", hassioRuleExecutionEvent.datetime);
        hassioLightState.attributes.brightness = 254;

        if(this.flash) {
            hassioLightState.attributes.flash = "short";
        }

        List<Float> rgb_colors = new ArrayList<>();
        rgb_colors.add((float) this.color.getRed() / 255.0f);
        rgb_colors.add((float) this.color.getGreen() / 255.0f);
        rgb_colors.add((float) this.color.getBlue() / 255.0f);

        hassioLightState.attributes.rgb_color = rgb_colors;
        hassioLightState.context = new HassioContext();

        newStates.add(hassioLightState);

        return newStates;
    }
}