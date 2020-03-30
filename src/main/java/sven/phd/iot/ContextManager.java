package sven.phd.iot;

import sven.phd.iot.api.resources.StateResource;
import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.students.mathias.states.HassioConflictSolutionState;
import sven.phd.iot.students.mathias.states.HassioConflictState;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.study.StudyManager;
import sven.phd.iot.predictions.Future;
import sven.phd.iot.predictions.PredictionEngine;
import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;

import java.util.*;

public class ContextManager {
    private static ContextManager contextManager;
    private HassioDeviceManager hassioDeviceManager;
    private RulesManager rulesManager;
    private PredictionEngine predictionEngine;
    private StudyManager studyManager;

    private ContextManager() {
        this.rulesManager = new RulesManager();
        this.hassioDeviceManager = new HassioDeviceManager(this);
        this.predictionEngine = new PredictionEngine(rulesManager, this.hassioDeviceManager);
        this.studyManager = new StudyManager(this);

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
        return this.predictionEngine.getFuture().futureExecutions;
    }

    /**
     * Get the future rule executions of a specific rule
     * @return
     */
    public List<HassioRuleExecutionEvent> getFutureRuleExecutions(String id) {
        return this.predictionEngine.getFuture().getExecutionFuture(id);
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
        return this.predictionEngine.getFuture().getFutureStates();
    }

    /**
     * Get the future of a single device
     * @return
     */
    public List<HassioState> getStateFuture(String id) {
        return this.predictionEngine.getFuture().getFutureStates(id);
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
        this.updateFuturePredictions();
    }

    public void updateFuturePredictions() {
        this.predictionEngine.updateFuturePredictions();
        StateResource.getInstance().broadcastRefresh();
    }

    /**
     * Check which rules need to be triggered and execute them IMMEDIATELY
     */
    private void executeRules(HassioChange hassioChange) {
        HashMap<String, HassioState> hassioStates = this.getHassioStates();

        List<HassioRuleExecutionEvent> triggerEvents = rulesManager.verifyTriggers(hassioStates, hassioChange);

        for(HassioRuleExecutionEvent triggerEvent : triggerEvents) {
            List<HassioState> resultingActions = triggerEvent.getTrigger().simulate(triggerEvent, hassioStates);

            // Apply additional changes as result of the rules as the new state
            List<HassioContext> contexts = this.hassioDeviceManager.setHassioDeviceStates(resultingActions);

            triggerEvent.addActionContexts(contexts);
            triggerEvent.getTrigger().logHassioRuleExecutionEvent(triggerEvent);
        }
    }

    /**
     * Print the behavior of the system (set of rules) to a string
     * @return
     */
    public String printRules() {
        return this.rulesManager.printRulesToString();
    }

    public Trigger getRuleById(String ruleId) {
        return this.rulesManager.getRuleById(ruleId);
    }

    public Map<String, Trigger> getRules() {
        return this.rulesManager.getRules();
    }

    public Trigger getRule(String id) {
        return this.rulesManager.getRule(id);
    }

    public void updateRule(String id, boolean enabled, boolean available) {
        this.getRule(id).setEnabled(enabled);
        this.getRule(id).setAvailable(available);
        ContextManager.getInstance().updateFuturePredictions();
    }

    public Future simulateAlternativeFuture(HashMap<String, Boolean> simulatedRulesEnabled, List<HassioState> simulatedStates) {
        return this.predictionEngine.whatIf(simulatedRulesEnabled, simulatedStates);
    }

    public StudyManager getStudyManager() {
        return studyManager;
    }

    public RulesManager getRulesManager() {
        return rulesManager;
    }

    public PredictionEngine getPredictionEngine() {
        return this.predictionEngine;
    }

    /**
     * Get the cached version of the future conflicts
     * @return
     */
    public List<HassioConflictState> getFutureConflicts() {
        return this.predictionEngine.getFuture().getFutureConflicts();
    }

    /**
     * Get the cached version of the future conflicts of a single device
     * @param id
     * @return
     */
    public List<HassioConflictState> getFutureConflicts(String id) {
        return this.predictionEngine.getFuture().getFutureConflicts(id);
    }

    /*****************************************************************************************/
    /**
     * Get the cached version of the future conflict solutions
     * @return
     */
    public List<HassioConflictSolutionState> getFutureConflictSolutions() {
        return this.predictionEngine.getFuture().getFutureConflictSolutions();
    }

    /**
     * Get the cached version of the future conflicts of a single device
     * @param id
     * @return
     */
    public List<HassioConflictSolutionState> getFutureConflictSolutions(String id) {
        return this.predictionEngine.getFuture().getFutureConflictSolutions(id);
    }
}