package sven.phd.iot.predictions;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.updates.ExecutionEvent;
import sven.phd.iot.students.mathias.ConflictSolver;
import sven.phd.iot.students.mathias.states.ConflictSolution;
import sven.phd.iot.students.mathias.states.Conflict;
import sven.phd.iot.hassio.states.HassioState;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Future {
   // @JsonProperty("states") private List<HassioState> futureStates;
    @JsonProperty("states_causal_stack") private CausalStack causalStack;

    @JsonProperty("executions") public List<ExecutionEvent> futureExecutions;
    @JsonProperty("conflicts") public List<Conflict> futureConflicts;
    @JsonProperty("conflict_solutions") public List<ConflictSolution> futureConflictSolutions;
    @JsonProperty("last_generated") public Date lastGenerated;


    public Future() {
       // this.futureStates = new ArrayList<>();
        this.causalStack = new CausalStack();
        this.futureExecutions = new ArrayList<>();
        this.futureConflicts = new ArrayList<>();
        this.futureConflictSolutions = new ArrayList<>();
        this.lastGenerated = new Date();
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

    /** Add another layer to the future
     *
     * @param layer
     */
    public void addCausalLayer(CausalLayer layer) {
        this.causalStack.addLayer(layer);
        layer.print();
    }

    /**
     * Get a cached version of the prediction of the future executions
     */
    public List<ExecutionEvent> getFutureExecutions() {
        return this.futureExecutions;
    }

    /**
     * Get a cached version of the prediction of the future states
     */
    public List<HassioState> getFutureStates() {
        return this.causalStack.flattenChanges();
    }

    /**
     * Get a cached version of the prediction of the future states for a single device
     */
    public List<HassioState> getFutureStates(String deviceID) {
        List<HassioState> result = new ArrayList<>();

        for(HassioState hassioState : this.getFutureStates()) {
            if(hassioState.entity_id.equals(deviceID)) {
                result.add(hassioState);
            }
        }

        return result;
    }

    /**
     * Add a future state to the list of predictions
     * @param newState
     */
   /* public void addFutureState(HassioState newState) {
        this.futureStates.add(newState);
    } */



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
}