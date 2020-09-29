package sven.phd.iot.overrides;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.states.HassioState;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OverridesManager {
    @JsonProperty("snoozed_actions") public List<SnoozedAction> snoozedActions;
    @JsonProperty("custom_states") public List<HassioState> customStates;

    private ContextManager contextManager;

    public OverridesManager(ContextManager contextManager) {
        this.snoozedActions = new ArrayList<>();
        this.customStates = new ArrayList<>();
        this.contextManager = contextManager;

        this.snoozedActions.add(new SnoozedAction("action1", new Date(), 1000, "sun.sun"));
    }

    public void addCustomState(HassioState hassioState) {
        this.customStates.add(hassioState);
        this.contextManager.updateFuturePredictions();
    }

    public void addSnoozedAction(SnoozedAction snoozedAction) {
        this.snoozedActions.add(snoozedAction);
        this.contextManager.updateFuturePredictions();
    }

    public void removeSnoozedAction(SnoozedAction snoozedAction) {
        // TODO:
    }

    public List<SnoozedAction> getSnoozedActions() {
        return this.snoozedActions;
    }

    public List<HassioState> getScheduledStates() {
        return this.customStates;
    }

    public void cleanSolutions() {
        this.customStates.clear();
        this.snoozedActions.clear();
        this.contextManager.updateFuturePredictions();
    }
}