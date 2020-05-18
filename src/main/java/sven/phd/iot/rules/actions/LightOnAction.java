package sven.phd.iot.rules.actions;

import sven.phd.iot.hassio.light.HassioLightAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.Action;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LightOnAction extends Action {
    private String deviceIdentifier;
    private Color color;
    private boolean flash;

    public LightOnAction(String description, String deviceIdentifier,  Color color, boolean flash) {
        // super("Turn on " + friendlyName + " {color: [" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "], flash: " + flash + "}");
        super(description);
        this.deviceIdentifier = deviceIdentifier;

        if(color == null) {
            color = Color.YELLOW;
        }

        this.color = color;
        this.flash = flash;
    }

    public List<HassioState> simulate(HassioRuleExecutionEvent hassioRuleExecutionEvent, HashMap<String, HassioState> hassioStates) {
        List<HassioState> newStates = new ArrayList<>();

        if (!isEnabled(hassioRuleExecutionEvent.datetime)) {
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

        newStates.add(new HassioState(deviceIdentifier, "on", hassioRuleExecutionEvent.datetime, attributes));

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
}