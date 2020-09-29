package sven.phd.iot;

import sven.phd.iot.api.resources.StateResource;
import sven.phd.iot.conflicts.ConflictVerificationManager;
import sven.phd.iot.rules.*;
import sven.phd.iot.conflicts.OverridesManager;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.students.mathias.StudyManagerMathias;
import sven.phd.iot.conflicts.Conflict;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.scenarios.ScenarioManager;
import sven.phd.iot.study.StudyManager;
import sven.phd.iot.predictions.Future;
import sven.phd.iot.predictions.PredictionEngine;

import java.util.*;

public class ContextManager {
    private static ContextManager contextManager;
    private HassioDeviceManager hassioDeviceManager;
    private RulesManager rulesManager;
    private PredictionEngine predictionEngine;
    private ScenarioManager scenarioManager;
    //private StudyManager studyManager;
    private StudyManagerMathias studyManager;
    private OverridesManager overridesManager;
    private ConflictVerificationManager conflictVerificationManager;

    private ContextManager() {
        this.rulesManager = new RulesManager();
        this.hassioDeviceManager = new HassioDeviceManager(this);
        this.overridesManager = new OverridesManager(this);
        this.conflictVerificationManager = new ConflictVerificationManager();
        this.predictionEngine = new PredictionEngine(rulesManager, this.hassioDeviceManager, this.overridesManager, this.conflictVerificationManager);
        this.scenarioManager = new ScenarioManager(this);
        //this.studyManager = new StudyManager();
        this.studyManager = new StudyManagerMathias();

        // NEVER EVER START PREDICTING WHEN LAUNCHING THIS SHIT
    }

    /**
     * Singleton, there is only one context
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

    public OverridesManager getOverridesManager() { return this.overridesManager; }

    /**
     * Get the past rule executions
     * @return
     */
    public List<RuleExecution> getPastRuleExecutions() {
        return this.rulesManager.getPastRuleExecutions();
    }

    /**
     * Get the past rule executions of a specific rule
     * @return
     */
    public List<RuleExecution> getPastRuleExecutions(String id) {
        return this.rulesManager.getPastRuleExecutions(id);
    }

    /**
     * Get the future rule executions
     * @return
     */
    public List<RuleExecution> getFutureRuleExecutions() {
        return this.predictionEngine.getFuture().getFutureExecutions();
    }

    /**
     * Get the future rule executions of a specific rule
     * @return
     */
    public List<RuleExecution> getFutureRuleExecutions(String id) {
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
        List<HassioChange> hassioChanges = new ArrayList<>();
        hassioChanges.add(hassioChange);

        List<RuleExecution> triggerEvents = rulesManager.verifyTriggers(new Date(), hassioStates, hassioChanges, new HashMap<>());
        List<RuleExecution> conditionTrueEvents = rulesManager.verifyConditions(hassioStates, triggerEvents);

        for(RuleExecution conditionTrueEvent : conditionTrueEvents) {
            Trigger rule = rulesManager.getRuleById(conditionTrueEvent.ruleID);
            HashMap<String, List<HassioState>> resultingActions = rule.simulate(conditionTrueEvent, hassioStates, new ArrayList<>());

            // Apply changes as result of the rules as the new state
            for(String actionID : resultingActions.keySet()) {
                conditionTrueEvent.addActionExecution(new ActionExecution(new Date(), actionID, resultingActions.get(actionID)));
            }

            rule.logHassioRuleExecutionEvent(conditionTrueEvent);
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

    public ScenarioManager getScenarioManager() {
        return scenarioManager;
    }

    public RulesManager getRulesManager() {
        return rulesManager;
    }

    public PredictionEngine getPredictionEngine() {
        return this.predictionEngine;
    }

    public StudyManager getStudyManager() { return this.studyManager; }

    /**
     * Get the cached version of the future conflicts
     * @return
     */
    public List<Conflict> getFutureConflicts() {
        return this.predictionEngine.getFuture().getFutureConflicts();
    }

    /**
     * Get the cached version of the future conflicts of a single device
     * @param id
     * @return
     */
    public List<Conflict> getFutureConflicts(String id) {
        return this.predictionEngine.getFuture().getFutureConflicts(id);
    }

    public List<Action> getAllActionsOnDevice(String id){
        List<Action> result = new ArrayList<>();
        HassioDevice device = this.hassioDeviceManager.getDevice(id);

        if (device == null) {
            return result;
        }

        return device.getAllActions();

    }

    public Action getActionById(String id) {
        Map<String, Action> allActions = rulesManager.getAllActions();
       // allActions.putAll(actionExecutions.getAllActions());
        for (String actionID : allActions.keySet()) {
            if (actionID.equals(id)) {
                return allActions.get(actionID);
            }
        }
        return null;
    }

    public ConflictVerificationManager getConflictVerificationManager() {
        return conflictVerificationManager;
    }

    public Future getFuture() {
        return this.predictionEngine.getFuture();
    }
}