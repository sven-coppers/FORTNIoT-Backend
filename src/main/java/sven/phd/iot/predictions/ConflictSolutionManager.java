package sven.phd.iot.predictions;

import sven.phd.iot.students.mathias.states.Conflict;
import sven.phd.iot.students.mathias.states.ConflictSolution;
import sven.phd.iot.students.mathias.states.ConflictingAction;

import java.util.ArrayList;
import java.util.List;

public class ConflictSolutionManager {
    private List<ConflictSolution> conflictSolutionList;

    public ConflictSolutionManager() {
        this.conflictSolutionList = new ArrayList<>();

        ConflictSolution testConflictSolution = new ConflictSolution("light.living_spots");

        ConflictingAction lichtOffAction = new ConflictingAction("actionId2", "rule.nobody_home_lights");
        ConflictingAction lichtOnAction = new ConflictingAction("actionId4", "rule.blinds_up");

        testConflictSolution.addConflictingAction(lichtOffAction);
        testConflictSolution.addConflictingAction(lichtOnAction);

        testConflictSolution.snoozeAction(lichtOffAction, true);
        testConflictSolution.snoozeAction(lichtOnAction, false);

        this.addSolution(testConflictSolution);
    }

    public void addSolution(ConflictSolution conflictSolution) {
        this.conflictSolutionList.add(conflictSolution);
    }

    public List<ConflictSolution> getSolutions() {
        return this.conflictSolutionList;
    }

    public ConflictSolution getSolutionForConflict(Conflict conflict) {
        for(ConflictSolution solution : this.conflictSolutionList) {
            if(solution.matchesConflict(conflict)) {
                return solution;
            }
        }

        return null;
    }
}