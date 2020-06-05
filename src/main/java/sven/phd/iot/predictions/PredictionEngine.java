package sven.phd.iot.predictions;
import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.hassio.updates.ImplicitBehaviorEvent;
import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.students.mathias.FutureConflictDetector;

import java.util.*;

public class PredictionEngine {
    private HassioStateScheduler stateScheduler;
    private RulesManager rulesManager;
    private HassioDeviceManager hassioDeviceManager;
    private Future future;
    private Boolean predicting;
    private long tickRate = 5; // minutes
    private long predictionWindow = 1 * 24 * 60; // 1 day in minutes

    public PredictionEngine(RulesManager rulesManager, HassioDeviceManager hassioDeviceManager) {
        this.rulesManager = rulesManager;
        this.hassioDeviceManager = hassioDeviceManager;
        this.stateScheduler = hassioDeviceManager.getStateScheduler();
        this.future = new Future();
        this.predicting = false;
    }

    public Future getFuture() {
        return future;
    }

    /**
     * Predict the future with the latest information we have
     */
    public void updateFuturePredictions() {
        this.future = predictFuture(new HashMap<>(), new ArrayList<>());
    }

    /**
     * Predict an alternative future with simulated input
     * @param simulatedRulesEnabled a hashmap that holds a boolean (enabled) for every rule
     * @param simulatedStates a list of additional states
     */
    public Future whatIf(HashMap<String, Boolean> simulatedRulesEnabled, List<HassioState> simulatedStates) {
        return predictFuture(simulatedRulesEnabled, simulatedStates);
    }

    /**
     * Predict the future states and event of each HassioDevice and each rule
     * @post: Each HassioDevice and Each Rule will have a cached version of the outcome
     * TODO: This engine is still sensitive to loops and race conditions
     */
    private Future predictFuture(HashMap<String, Boolean> simulatedRulesEnabled, List<HassioState> simulatedStates) {
        Future future = new Future();

        // Initialise the queue with changes we already know
        PriorityQueue<HassioState> queue = new PriorityQueue<>();

        HashMap<String, HassioState> lastStates = hassioDeviceManager.getCurrentStates();
        queue.addAll(hassioDeviceManager.predictFutureStates());
        queue.addAll(simulatedStates);
        queue.addAll(stateScheduler.getScheduledStates());

        Date lastFrameDate = new Date(); // Prediction start
        Date predictionEnd = new Date(new Date().getTime() + getPredictionWindow() * 60l * 1000l); // Convert prediction window from minutes to milliseconds

        // Predict the first day with high precision
        while(lastFrameDate.getTime() < predictionEnd.getTime()) {
            Date nextTickDate = new Date(lastFrameDate.getTime() + (getTickRate() * 60l * 1000l)); // Convert tickrate from minutes to milliseconds

            // If there is an element in the queue that will happen before the tick
            if(!queue.isEmpty() && queue.peek().getLastChanged().getTime() < nextTickDate.getTime()) {
                nextTickDate = queue.peek().getLastChanged();
            }

            this.tick(nextTickDate, lastStates, queue, future, simulatedRulesEnabled);
            lastFrameDate = nextTickDate;
        }

        // Finish predicting the rest of the queue (within the prediction window)
        while(!queue.isEmpty()) {
            if(queue.peek().getLastChanged().getTime() < predictionEnd.getTime()) {
                this.tick(queue.peek().getLastChanged(), lastStates, queue, future, simulatedRulesEnabled);
            } else {
                queue.poll();
            }
        }

        System.out.println("Predictions updated: " + future.getFutureStates().size());

        // MATHIAS TESTING
        FutureConflictDetector detector = new FutureConflictDetector();
        future.addFutureConflict(detector.getFutureConflicts(future));

        return future;
    }

    private void tick(Date newDate, HashMap<String, HassioState> lastStates, PriorityQueue<HassioState> globalQueue, Future future, HashMap<String, Boolean> simulatedRulesEnabled) {
        // Build local Queue (bevat geen conflicten, maar kan wel conflicten veroorzaken)
        CausalNode causalTree = null;
        LinkedList<CausalNode> tempQueue = new LinkedList<CausalNode>();
        CausalNode lastAddedNode = causalTree;

        // IMPLICIT Let the devices predict their state, based on the past states (e.g. temperature)
        if(this.isPredicting()) {
            List<ImplicitBehaviorEvent> behaviorEvents = hassioDeviceManager.predictImplictStates(newDate, lastStates);

            // Add changes to prediction queue
            for(ImplicitBehaviorEvent behaviorEvent : behaviorEvents) {
                behaviorEvent.setTrigger(this.rulesManager.getRuleById(this.rulesManager.RULE_IMPLICIT_BEHAVIOR));

                for(HassioState newState : behaviorEvent.getActionStates()) {
                    tempQueue.add(new CausalNode(newState, behaviorEvent));
                }

                // Add Implicit behavior to the future
                // TODO: POSTPONE TO END OF THE TICK
                //future.addHassioRuleExecutionEventPrediction((HassioRuleExecutionEvent) behaviorEvent);
            }
        }

        // BASELINE: Add states from the global queue (before the current date), which could induce conflicts
        while(!globalQueue.isEmpty() && globalQueue.peek().getLastChanged().getTime() <= newDate.getTime()) {
            HassioState newState = globalQueue.poll();

            tempQueue.add(new CausalNode(newState, null));

            // TODO: POSTPONE TO END OF THE TICK
            //future.addFutureState(newState);
        }

        // The first element becomes the root, the rest will be added to its queue
        if(!tempQueue.isEmpty()) {
            causalTree = tempQueue.pop();
            causalTree.pushToQueue(tempQueue);

            // Werk verder tot ieder blad een lege queue heeft
            LinkedList<CausalNode> leafQueue = new LinkedList<CausalNode>();
            leafQueue.add(causalTree);

            if(this.isPredicting()) {
                while (!leafQueue.isEmpty()) {
                    CausalNode potentialLeaf = handleLeaf(newDate, lastStates, leafQueue.pop(), simulatedRulesEnabled);

                    if(potentialLeaf != null) {
                        leafQueue.add(potentialLeaf);
                    }
                }
            }

            System.out.println("Tick " + newDate);
            causalTree.print();
        }


        // Conflict detection: inconsistency


        // ---- BEGIN: We cannot confirm this yet
        // Add predicted executions to the rule's prediction list
       /* future.addHassioRuleExecutionEventPrediction(potentialTriggerEvent);

        // Add the actions to the prediction QUEUEs
        for(String actionID : resultingActions.keySet()) {
            globalQueue.addAll(resultingActions.get(actionID));
        } */
        // ----- END: We cannot confirm this yet

        // TODO
        // SOLUTIONS gebruiken om de boom te prunen
        // Overgebleven boom committen we als predicted states
    }

    /**
     *
     * @param lastStates
     * @param oldLeaf
     * @param simulatedRulesEnabled
     * @return a newly created leaf if there is one, null otherwise
     * @post TODO: the newly created leaf is already placed at the right place in the tree
     */
    private CausalNode handleLeaf(Date newDate, HashMap<String, HassioState> lastStates, CausalNode oldLeaf, HashMap<String, Boolean> simulatedRulesEnabled) {
        // Build the states from this branch
        HashMap<String, HassioState> branchSpecificStates = buildBranchSpecificStates(lastStates, oldLeaf);

        // Build a change
        HassioState newState = oldLeaf.getState();
        HassioState lastState = lastStates.get(newState.entity_id);
        HassioChange newChange = new HassioChange(newState.entity_id, lastState, newState, newState.getLastChanged());

        // Pass the stateChange to the set of rules
        List<HassioRuleExecutionEvent> potentialTriggerEvents = this.rulesManager.verifyTriggers(branchSpecificStates, newChange, simulatedRulesEnabled);
        for(HassioRuleExecutionEvent potentialTriggerEvent : potentialTriggerEvents) {
            HashMap<String, List<HassioState>> proposedActionState = potentialTriggerEvent.getTrigger().simulate(potentialTriggerEvent, branchSpecificStates); // ZEKER

            // For every proposed action, add it to the queue
            for(String actionID : proposedActionState.keySet()) {
                for(HassioState proposedState : proposedActionState.get(actionID)) {
                    oldLeaf.pushToQueue(new CausalNode(proposedState, potentialTriggerEvent));
                }
            }

            // Add the context of the simulated actions as a result in the potentialTriggerEvent
            for(String actionID : proposedActionState.keySet()) {
                List<HassioContext> resultingContexts = new ArrayList<>();

                for(HassioState proposedState : proposedActionState.get(actionID)) {
                    resultingContexts.add(proposedState.context);
                }

                potentialTriggerEvent.addActionExecuted(actionID, resultingContexts);
            }
        }

        // Pass the stateChange to the implicit rules (e.g. turn heater on/off)
        List<ImplicitBehaviorEvent> behaviorEvents = hassioDeviceManager.predictImplicitRules(newDate, lastStates);
        for(ImplicitBehaviorEvent behaviorEvent : behaviorEvents) {
            behaviorEvent.setTrigger(this.rulesManager.getRuleById(this.rulesManager.RULE_IMPLICIT_BEHAVIOR));

            for(HassioState newActionState : behaviorEvent.getActionStates()) {
                oldLeaf.pushToQueue(new CausalNode(newActionState, behaviorEvent));
            }
        }

        // Check if this leaf is finished
        if(oldLeaf.peekFromQueue() == null) {
            return null; // This is a finished leaf
        } else {
            // Not yet -> create a leaf as a child
            CausalNode newLeaf = oldLeaf.popFromQueue();
            newLeaf.pushToQueue(oldLeaf.getQueue());
            oldLeaf.chainNode(newLeaf);

            return newLeaf;
        }
    }

    /**
     * Build a hasmap with the last states, updated with all changes that are specific to this branch in the three
     * @param lastStates
     * @param causalNode
     * @return
     */
    private HashMap<String, HassioState> buildBranchSpecificStates(HashMap<String, HassioState> lastStates, CausalNode causalNode) {
        HassioState newState = causalNode.getState();

        HashMap<String, HassioState> branchSpecificStates = new HashMap<>();
        for(String deviceID : lastStates.keySet()) {
            branchSpecificStates.put(deviceID, lastStates.get(deviceID));
        }

        branchSpecificStates.put(newState.entity_id, newState);

        CausalNode ancestor = causalNode;

        while(ancestor != null && ancestor.getState() != null) {
            branchSpecificStates.put(ancestor.getState().entity_id, ancestor.getState());
            ancestor = ancestor.getParent();
        }

        return branchSpecificStates;
    }

    public void stopFuturePredictions() {
        this.predicting = false;
        this.updateFuturePredictions(); // Generate a new future without simulating rules
    }

    public void startFuturePredictions() {
        this.predicting = true;
        this.updateFuturePredictions();
    }

    public boolean isPredicting() {
        return this.predicting;
    }

    public long getTickRate() {
        return tickRate;
    }

    public void setTickRate(long tickRate) {
        this.tickRate = tickRate;
        this.updateFuturePredictions();
    }

    public long getPredictionWindow() {
        return predictionWindow;
    }

    public void setPredictionWindow(long predictionWindow) {
        this.predictionWindow = predictionWindow;
        this.updateFuturePredictions();
    }
}