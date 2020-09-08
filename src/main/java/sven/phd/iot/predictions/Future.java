package sven.phd.iot.predictions;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.updates.ExecutionEvent;
import sven.phd.iot.students.mathias.ConflictSolver;
import sven.phd.iot.students.mathias.states.ConflictSolution;
import sven.phd.iot.students.mathias.states.Conflict;
import sven.phd.iot.hassio.states.HassioState;

import java.util.*;

public class Future {
   // @JsonProperty("states") private List<HassioState> futureStates;
   // @JsonProperty("states_causal_stack") private CausalStack causalStack;

    /** For convenience in conflict detection algorithms, the first state of every entity is the CURRENT state */
    @JsonProperty("states_causal_map") private HashMap<String, Stack<CausalNode>> causalNodeListMap;

    @JsonProperty("executions") public List<ExecutionEvent> futureExecutions;
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
      //  this.causalStack = new CausalStack();
        this.futureExecutions = new ArrayList<>();
        this.futureConflicts = new ArrayList<>();
        this.futureConflictSolutions = new ArrayList<>();
        this.lastGenerated = new Date();

        this.initCausalNodeListMap(initialStates);
    }

    private void initCausalNodeListMap(HashMap<String, HassioState> initialStates) {
        this.causalNodeListMap = new HashMap<>();

        for(String entityID : initialStates.keySet()) {
            causalNodeListMap.put(entityID, new Stack<>());
            causalNodeListMap.get(entityID).add(new CausalNode(initialStates.get(entityID), null));
        }
    }

    /**
     * Get the future executions of this rule
     */
    public List<ExecutionEvent> getExecutionFuture(String entityID) {
        List<ExecutionEvent> result = new ArrayList<>();

        for(ExecutionEvent executionEvent : this.futureExecutions) {
            if(executionEvent.entity_id.equals(entityID)) {
                result.add(executionEvent);
            }
        }

        return result;
    }

    /**
     * Allow the prediction engine to add a predicted triggerEvent of this rule
     * @param triggerEvent
     */
    public void addExecutionEvent(ExecutionEvent triggerEvent) {
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
    public List<ExecutionEvent> getFutureExecutions() {
        return this.futureExecutions;
    }

    /**
     * Get a sorted list (from old to new) of ALL future states
     * The first state for each entity is the current state, and should be filtered out
     */
    public List<HassioState> getFutureStates() {
        List<HassioState> result = new ArrayList<>();
        PriorityQueue<HassioState> queue = new PriorityQueue<>();

        // Insert all states in an ordered queue, which will automatically sort all states regardless of their entity
        for(String entityID : this.causalNodeListMap.keySet()) {
            for(int i = 1; i < this.causalNodeListMap.get(entityID).size(); ++i) {
                queue.add(this.causalNodeListMap.get(entityID).get(i).getState());
                    System.out.println("\t" + this.causalNodeListMap.get(entityID).get(i).getState().getLastUpdated() + " " + this.causalNodeListMap.get(entityID).get(i).getState().state);
            }
        }

        // Use an iterator to maintain the sorted order after the conversion to an arraylist

        System.out.println("Now for the queue");
        while (!queue.isEmpty()) {
            HassioState state = queue.poll();
            System.out.println("\tQueue:" + state.getLastUpdated() + " " + state.state);
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

        if(this.causalNodeListMap.containsKey(entityID)) {
            for(int i = 1; i < this.causalNodeListMap.get(entityID).size(); ++i) {
                result.add(this.causalNodeListMap.get(entityID).get(i).getState());
            }
        }

        return result;
    }

    /**
     * Add a list of future changes THAT will happen at the same time to the list of predictions
     * @param newNodes the list of future changes
     */
    public void addFutureStates(List<CausalNode> newNodes) {
        for(CausalNode causalNode : newNodes) {
            this.causalNodeListMap.get(causalNode.getState().entity_id).push(causalNode);
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
            if(hassioConflict.conflictingEntities.contains(deviceID)) {
                result.add(hassioConflict);
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
    public HashMap<String, CausalNode> getLastStates() {
        HashMap<String, CausalNode> lastStates = new HashMap<>();

        for(String entityID: this.causalNodeListMap.keySet()) {
            lastStates.put(entityID, this.causalNodeListMap.get(entityID).peek());
        }

        return lastStates;
    }
}