package sven.phd.iot;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioEvent;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.hassio.updates.HassioUpdate;
import sven.phd.iot.predictions.PredictionEngine;
import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ContextManager {
    private static ContextManager contextManager;
    private HassioDeviceManager hassioDeviceManager;
    private RulesManager rulesManager;
    private PredictionEngine predictionEngine;

    private ContextManager() {
        this.rulesManager = new RulesManager();
        this.hassioDeviceManager = new HassioDeviceManager(this);
        this.predictionEngine = new PredictionEngine(rulesManager, this.hassioDeviceManager);

        // NEVER EVER START PREDICTING WHEN LAUNCHING THIS SHIT
    }

    /**
     * Singleton
     * @return
     */
    public static ContextManager getInstance() {
        if(contextManager == null) {
            contextManager = new ContextManager();
        }

        return contextManager;
    }

    public HassioDeviceManager getHassioDeviceManager() {
        return this.hassioDeviceManager;
    }

    /**
     * Get the past rule executions
     * @return
     */
    public List<HassioRuleExecutionEvent> getPastRuleExecutions() {
        return this.rulesManager.getPastRuleExecutions();
    }

    /**
     * Get the past rule executions of a specific rule
     * @return
     */
    public List<HassioRuleExecutionEvent> getPastRuleExecutions(String id) {
        return this.rulesManager.getPastRuleExecutions(id);
    }

    /**
     * Get the future rule executions
     * @return
     */
    public List<HassioRuleExecutionEvent> getFutureRuleExecutions() {
        return this.rulesManager.getFutureRuleExecutions();
    }

    /**
     * Get the future rule executions of a specific rule
     * @return
     */
    public List<HassioRuleExecutionEvent> getFutureRuleExecutions(String id) {
        return this.rulesManager.getFutureRuleExecutions(id);
    }

    /**
     * Get the state of the device with id = id
     * @param id
     * @return
     */
    public HassioState getHassioState(String id) {
        return this.hassioDeviceManager.getCurrentState(id);
    }

    /**
     * Get the current state of all devices
     * @return
     */
    public HashMap<String, HassioState> getHassioStates() {
        return this.hassioDeviceManager.getCurrentStates();
    }

    /**
     * Get the history of all device states
     * @return
     */
    public List<HassioState> getStateHistory() {
        return this.hassioDeviceManager.getStateHistory();
    }

    /**
     * Get the history of a single device
     * @return
     */
    public List<HassioState> getStateHistory(String id) {
        return this.hassioDeviceManager.getStateHistory(id);
    }

    /**
     * Get the cached version of the future states of each device
     * @return
     */
    public List<HassioState> getStateFuture() {
        return this.hassioDeviceManager.getStateFuture();
    }

    /**
     * Get the future of a single device
     * @return
     */
    public List<HassioState> getStateFuture(String id) {
        return this.hassioDeviceManager.getStateFuture(id);
    }

    /**
     * Get the history of all device events
     * @return
     */
    public List<HassioEvent> getEventHistory() {
        return this.hassioDeviceManager.getEventHistory();
    }

    /**
     * Get the cached version of the future events of each device
     * @return
     */
    public List<HassioEvent> getEventFuture() {
        return this.hassioDeviceManager.getEventFuture();
    }

    /**
     * Check what needs to happen when the state of a device has changed
     * @param hassioChange
     * @pre hassioChange has already been logged to the corresponding device
     */
    public void deviceChanged(HassioChange hassioChange) {
        // Execute rules (for real), and perform these changes
        this.executeRules(hassioChange);

        // Update all predictions
        this.predictionEngine.predictFuture();
    }

    /**
     * Check which rules need to be triggered and execute them IMMEDIATELY
     */
    private void executeRules(HassioChange hassioChange) {
        HashMap<String, HassioState> hassioStates = this.getHassioStates();

        List<HassioRuleExecutionEvent> triggerEvents = rulesManager.verifyTriggers(hassioStates, hassioChange);

        for(HassioRuleExecutionEvent triggerEvent : triggerEvents) {
            List<HassioState> resultingActions = triggerEvent.getTrigger().simulate(triggerEvent);

            // Apply additional changes as result of the rules as the new state
            List<HassioContext> contexts = this.hassioDeviceManager.setHassioDeviceStates(resultingActions);

            triggerEvent.addActionContexts(contexts);
            triggerEvent.getTrigger().logHassioRuleExecutionEvent(triggerEvent);
        }
    }

    /** GET ALL STATE CHANGES AND EVENTS, FROM HISTORY, NOW, AND, THE FUTURE
     * @return
     */
    public List<HassioUpdate> getAllUpdates() {
        List<HassioUpdate> hassioUpdates = new ArrayList<HassioUpdate>();

        // Add the history as the last part
        hassioUpdates.addAll(this.hassioDeviceManager.getStateHistory());
        hassioUpdates.addAll(this.hassioDeviceManager.getEventHistory());

        // Add predictions (from devices and additional simulations)
        hassioUpdates.addAll(this.hassioDeviceManager.getStateFuture());
        hassioUpdates.addAll(this.hassioDeviceManager.getEventFuture());

        Collections.sort(hassioUpdates);

        return hassioUpdates;
    }

    /**
     * Print the behavior of the system (set of rules) to a string
     * @return
     */
    public String getRules() {
        return this.rulesManager.printRulesToString();
    }

    public Trigger getRule(String id) {
        return this.rulesManager.getRule(id);
    }
}