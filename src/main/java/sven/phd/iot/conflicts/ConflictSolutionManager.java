package sven.phd.iot.conflicts;

import sven.phd.iot.rules.Action;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.scenarios.cases.InconsistencyDevices;
import sven.phd.iot.students.mathias.states.ConflictSolution;
import sven.phd.iot.students.mathias.states.ConflictingAction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ConflictSolutionManager {
    private List<ConflictSolution> conflictSolutionList;

    public ConflictSolutionManager() {
        this.conflictSolutionList = new ArrayList<>();

        ConflictSolution testConflictSolution = new ConflictSolution(InconsistencyDevices.LIVING_SPOTS);
/*
        ConflictingAction lichtOffAction = new ConflictingAction("actionId3", "rule.nobody_home_lights");
        ConflictingAction lichtOnAction = new ConflictingAction("actionId5", "rule.blinds_up");
  */
        // temporary test
        ConflictingAction lichtOffAction = new ConflictingAction("actionId3", "rule.nobody_home_lights");
        ConflictingAction lichtOnAction = new ConflictingAction("actionId1", "rule.sun_set");
        ConflictingAction lichtOnAction2 = new ConflictingAction("actionId10", "rule.test_trigger_living_spots");

        testConflictSolution.addConflictingAction(lichtOffAction);
        testConflictSolution.addConflictingAction(lichtOnAction);
        testConflictSolution.addConflictingAction(lichtOnAction2);

        testConflictSolution.snoozeAction(lichtOffAction, true);
        testConflictSolution.snoozeAction(lichtOnAction, true);
        testConflictSolution.snoozeAction(lichtOnAction2, true);

        testConflictSolution.addSolvingAction(new LightOnAction("Custom solution action", InconsistencyDevices.LIVING_SPOTS, Color.GREEN, true));

        //this.addSolution(testConflictSolution);
    }

    public void addSolution(ConflictSolution conflictSolution) {
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
    }

    public List<ConflictSolution> getSolutions() {
        return this.conflictSolutionList;
    }

    /**
     * Retrieves all the custom actions from every solution
     * @return
     */
    public List<Action> getSolutionActions() {
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
    }
}