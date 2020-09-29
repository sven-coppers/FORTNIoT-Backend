package sven.phd.iot.conflicts;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.students.mathias.states.SnoozedAction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

public class OverridesManager {
    @JsonProperty("snoozed_actions") public List<SnoozedAction> snoozedActions;
    @JsonProperty("custom_states") public List<HassioState> customStates;

    private ContextManager contextManager;

  //  private List<ConflictSolution> conflictSolutionList;

    public OverridesManager(ContextManager contextManager) {
        this.snoozedActions = new ArrayList<>();
        this.customStates = new ArrayList<>();
        this.contextManager = contextManager;

        this.snoozedActions.add(new SnoozedAction("action1", new Date(), 1000));

    //    this.conflictSolutionList = new ArrayList<>();

     //   ConflictSolution testConflictSolution = new ConflictSolution(InconsistencyDevices.LIVING_SPOTS);
/*
        ConflictingAction lichtOffAction = new ConflictingAction("actionId3", "rule.nobody_home_lights");
        ConflictingAction lichtOnAction = new ConflictingAction("actionId5", "rule.blinds_up");
  */
        // temporary test
      /*  SnoozedAction lichtOffAction = new SnoozedAction("actionId3", "rule.nobody_home_lights");
        SnoozedAction lichtOnAction = new SnoozedAction("actionId1", "rule.sun_set");
        SnoozedAction lichtOnAction2 = new SnoozedAction("actionId10", "rule.test_trigger_living_spots");

        testConflictSolution.addConflictingAction(lichtOffAction);
        testConflictSolution.addConflictingAction(lichtOnAction);
        testConflictSolution.addConflictingAction(lichtOnAction2);

        testConflictSolution.snoozeAction(lichtOffAction, true);
        testConflictSolution.snoozeAction(lichtOnAction, true);
        testConflictSolution.snoozeAction(lichtOnAction2, true);

        testConflictSolution.addSolvingAction(new LightOnAction("Custom solution action", InconsistencyDevices.LIVING_SPOTS, Color.GREEN, true)); */

        //this.addSolution(testConflictSolution);
    }

  /*  public void addSolution(ConflictSolution conflictSolution) {
        this.conflictSolutionList.add(conflictSolution);
    }

    public void removeSolution(Conflict conflict) {
        List<ConflictSolution> removeList = new ArrayList<>();
        for (ConflictSolution solution : this.conflictSolutionList) {
            if (solution.matchesConflict(conflict)) {
                removeList.add(solution);
            }
        }
        for (ConflictSolution remove : removeList) {
            conflictSolutionList.remove(remove);
        }
    } */

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

    public void performState() {
        // TODO: when it is time, perform the next state
    }

    public void cleanSolutions() {
        this.customStates.clear();
        this.snoozedActions.clear();
        this.contextManager.updateFuturePredictions();
    }

    /*

    /**
     * Retrieves all the custom actions from every solution
     * @return
     */
  /*  public List<Action> getSolutionActions() {
        List<Action> result = new ArrayList<>();
        for (ConflictSolution solution : conflictSolutionList) {
            result.addAll(solution.getCustomActions());
        }
        return result;
    }

    public ConflictSolution getSolutionForConflict(Conflict conflict) {
        for(ConflictSolution solution : this.conflictSolutionList) {
            if(solution.matchesConflict(conflict)) {
                return solution;
            }
        }

        return null;
    }

    public void removeRedundancySolutions(){
        List<ConflictSolution> removeList = new ArrayList<>();
        for (ConflictSolution solution : conflictSolutionList) {
            if (solution.isRedundancySolution()) {
                removeList.add(solution);
            }
        }
        conflictSolutionList.removeAll(removeList);
    }

    public void cleanSolutions() {
        conflictSolutionList.clear();
    } */
}