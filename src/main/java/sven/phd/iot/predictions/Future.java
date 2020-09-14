package sven.phd.iot.predictions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.rules.ActionExecution;
import sven.phd.iot.rules.RuleExecution;
import sven.phd.iot.students.mathias.states.ConflictSolution;
import sven.phd.iot.conflicts.Conflict;
import sven.phd.iot.hassio.states.HassioState;

import java.util.*;

public class Future {
    /** For convenience in conflict detection algorithms, the first state of every entity is the CURRENT state */
    @JsonProperty("states_causal_map") private HashMap<String, Stack<HassioState>> entityStateStackMap;
    @JsonProperty("executions") public Stack<RuleExecution> futureExecutions;
    @JsonProperty("conflicts") public List<Conflict> futureConflicts;
    @JsonProperty("conflict_solutions") public List<ConflictSolution> futureConflictSolutions;
    @JsonProperty("last_generated") public Date lastGenerated;

    /**
     * Default constructor
     */
    public Future() {
        new Future(new HashMap<>());
    }

    /**
     * Constructor when an initial set of states is known
     * @param initialStates
     */
    public Future(HashMap<String, HassioState> initialStates) {
        this.futureExecutions = new Stack<>();
        this.futureConflicts = new ArrayList<>();
        this.futureConflictSolutions = new ArrayList<>();
        this.lastGenerated = new Date();

        this.initCausalNodeListMap(initialStates);
    }

    /**
     * Set the initial state for each device as the first state. This is useful for conflict detection algorithms.
     * The first state is not exported as part of the future.
     * @param initialStates
     */
    private void initCausalNodeListMap(HashMap<String, HassioState> initialStates) {
        this.entityStateStackMap = new HashMap<>();

        for(String entityID : initialStates.keySet()) {
            entityStateStackMap.put(entityID, new Stack<>());
            entityStateStackMap.get(entityID).add(initialStates.get(entityID));
        }
    }

    /**
     * Get the future executions of this rule
     */
    public List<RuleExecution> getExecutionFuture(String entityID) {
        List<RuleExecution> result = new ArrayList<>();

        for(RuleExecution ruleExecution : this.futureExecutions) {
            if(ruleExecution.ruleID.equals(entityID)) {
                result.add(ruleExecution);
            }
        }

        return result;
    }

    /**
     * Allow the prediction engine to add a predicted triggerEvent of this rule
     * @param triggerEvent
     */
    public void addExecutionEvent(RuleExecution triggerEvent) {
        this.futureExecutions.add(triggerEvent);
    }

    /**
     * Remove part of the future by popping future stack until revertTime is reached
     * @param revertTime the moment that should be reverted to
     */
    public void revertTo(Date revertTime) {
        int numStatesReverted = 0;
        int numRuleExecutionsReverted = 0;
        int numConflictsReverted = 0;
        int numSolutionsReverted = 0;

        // TODO: Actually revert stuff
        System.out.println("/tBacktracking: " + numStatesReverted + " states reverted, " + numRuleExecutionsReverted + " rule executions reverted, " + numConflictsReverted + " conflicts reverted" + numSolutionsReverted + " solutions reverted");
    }

    /** Add another layer to the future
     *
     * @param layer
     */
   /* public void addCausalLayer(CausalLayer layer) {
        this.causalStack.addLayer(layer);
        layer.print();
    } */

    /**
     * Get a cached version of the prediction of the future executions
     */
    public List<RuleExecution> getFutureExecutions() {
        List<RuleExecution> ruleExecutions = new ArrayList<>();

        return ruleExecutions;

       // return this.futureExecutions;
    }

    /**
     * Get the execution event with this executionID
     * @param executionID
     * @return
     */
    public RuleExecution getExecution(String executionID) {
        for(RuleExecution event : this.futureExecutions) {
            if(event.ruleID.equals(executionID)) {
                return event;
            }
        }

        return null;
    }

    /**
     * Get a sorted list (from old to new) of ALL future states
     * The first state for each entity is the current state, and should be filtered out
     */
    public List<HassioState> getFutureStates() {
        List<HassioState> result = new ArrayList<>();
        PriorityQueue<HassioState> queue = new PriorityQueue<>();

        // Insert all states in an ordered queue, which will automatically sort all states regardless of their entity
        for(String entityID : this.entityStateStackMap.keySet()) {
            for(int i = 1; i < this.entityStateStackMap.get(entityID).size(); ++i) { // Skip the first item!
                queue.add(this.entityStateStackMap.get(entityID).get(i));
                //System.out.println("\t" + this.causalNodeListMap.get(entityID).get(i).getState().getLastUpdated() + " " + this.causalNodeListMap.get(entityID).get(i).getState().state);
            }
        }

        // maintain the sorted order after the conversion to an arraylist
        while (!queue.isEmpty()) {
            HassioState state = queue.poll();
            //System.out.println("\tQueue:" + state.getLastUpdated() + " " + state.state);
            result.add(state);
        }

        return result;
    }

    /**
     * Get a sorted list (from old to new) of the future states for a single entity
     * The first state for each entity is the current state, and should be filtered out
     */
    public List<HassioState> getFutureStates(String entityID) {
        List<HassioState> result = new ArrayList<>();

        if(this.entityStateStackMap.containsKey(entityID)) {
            for(int i = 1; i < this.entityStateStackMap.get(entityID).size(); ++i) { // Skip the first item!
                result.add(this.entityStateStackMap.get(entityID).get(i));
            }
        }

        return result;
    }

    /**
     * Get the second last state for an entity, useful when creating hassioChanges where
     * @param entityID
     * @return
     */
    public HassioState getSecondLastState(String entityID) {
        if(this.entityStateStackMap.containsKey(entityID)) {
            Stack<HassioState> stateStack = this.entityStateStackMap.get(entityID);

            return stateStack.get(stateStack.size() - 2);
        }

        return null;
    }

    /**
     * Add a list of future changes THAT will happen at the same time to the list of predictions
     * @param newState the new state future changes
     */
    public void addFutureState(HassioState newState) {
        if(this.entityStateStackMap.containsKey(newState.entity_id)) {
            this.entityStateStackMap.get(newState.entity_id).push(newState);
        } else {
            System.err.println("Could not add state = " + newState.state + " to entity " + newState.entity_id + " because the entity could not be found");
        }
    }

    /**
     * Get a cached version of the prediction of the future conflicts
     */
    public List<Conflict> getFutureConflicts() {
        return this.futureConflicts;
    }

    /**
     * Get a cached version of the prediction of the future conflicts for a single device
     */
    public List<Conflict> getFutureConflicts(String deviceID) {
        List<Conflict> result = new ArrayList<>();

        for(Conflict hassioConflict : this.futureConflicts) {
            for(HassioState conflictingState : hassioConflict.getConflictingStates()) {
                if(conflictingState.entity_id.equals(deviceID)) {
                    result.add(hassioConflict);
                }
            }
        }

        return result;
    }

    /**
     * Add a future conflict to the list of predictions
     * @param newConflict
     */
    public void addFutureConflict(Conflict newConflict) {
        this.futureConflicts.add(newConflict);
    }

    /**
     * Add future conflicts to the list of predictions
     * @param newConflicts
     */
    public void addFutureConflicts(List<Conflict> newConflicts) {this.futureConflicts.addAll(newConflicts); }

    /**
     * Get a cached version of the future conflict solutions
     */
    public List<ConflictSolution> getFutureConflictSolutions() {
        return this.futureConflictSolutions;
    }

    /**
     * Get a cached version of the future conflict solutions for a single device
     */
    public List<ConflictSolution> getFutureConflictSolutions(String deviceID) {
        List<ConflictSolution> result = new ArrayList<>();

        for(ConflictSolution hassioSolution : this.futureConflictSolutions) {
            if(hassioSolution.entity_id.equals(deviceID)) {
                result.add(hassioSolution);
            }
        }

        return result;
    }

    /**
     * Add a future conflict solution to the list
     * @param newConflictSolution
     */
    public void addFutureConflictSolution(ConflictSolution newConflictSolution) {
        this.futureConflictSolutions.add(newConflictSolution);
    }

    /**
     * Set future conflict solutions
     * @param newConflictSolutions
     */
    public void setFutureConflictSolutions(List<ConflictSolution> newConflictSolutions) {this.futureConflictSolutions = newConflictSolutions; }

    /**
     * For each entity, get the most up-to-date prediction
     * @return
     */
    @JsonIgnore
    public HashMap<String, HassioState> getLastStates() {
        HashMap<String, HassioState> lastStates = new HashMap<>();

        for(String entityID: this.entityStateStackMap.keySet()) {
            lastStates.put(entityID, this.entityStateStackMap.get(entityID).peek());
        }

        return lastStates;
    }

    /**
     * Get the number of deduced predictions (states WITH an execution event)
     * @return
     */
    public int getNumDeducedPredictions() {
        int result = 0;

        for(RuleExecution ruleExecution : this.futureExecutions) {
            for(ActionExecution actionExecution : ruleExecution.getActionExecutions()) {
                result += actionExecution.resultingContexts.size();
            }
        }

        return result;
    }

    /**
     * Get the number of self-sustaining predictions (states WITHOUT an execution event)
     * @return
     */
    public int getNumSelfSustainingPredictions() {
        return this.getFutureStates().size() - this.getNumDeducedPredictions();
    }

    public Stack<HassioState> getEntityStateStack(String entity_id) {
        if(this.entityStateStackMap.containsKey(entity_id)) {
            return entityStateStackMap.get(entity_id);
        }

        return null;
    }
}