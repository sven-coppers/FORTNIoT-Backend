package sven.phd.iot.students.bram.rules.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.light.HassioLightState;
import sven.phd.iot.hassio.outlet.HassioOutletState;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.List;

public class OutletOnAction extends Action {
    @JsonProperty("entity_id")
    private final String deviceIdentifier;

    public OutletOnAction(String deviceIdentifier) {
        super("Turn on " + deviceIdentifier);
        this.deviceIdentifier = deviceIdentifier;
    }

    public List<HassioState> simulate(HassioRuleExecutionEvent hassioRuleExecutionEvent) {
        List<HassioState> newStates = new ArrayList<>();

        System.out.println("Simulating turn on of " + this.deviceIdentifier);
        HassioOutletState hassioOutletState = new HassioOutletState(deviceIdentifier, "on", hassioRuleExecutionEvent.datetime);
        hassioOutletState.context = new HassioContext();

        newStates.add(hassioOutletState);

        return newStates;
    }
}
