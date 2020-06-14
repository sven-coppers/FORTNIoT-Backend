package sven.phd.iot.predictions;

import sven.phd.iot.rules.Action;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.scenarios.cases.InconsistencyDevices;
import sven.phd.iot.students.mathias.states.Conflict;
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

        ConflictingAction lichtOffAction = new ConflictingAction("actionId3", "rule.nobody_home_lights");
        ConflictingAction lichtOnAction = new ConflictingAction("actionId5", "rule.blinds_up");

        testConflictSolution.addConflictingAction(lichtOffAction);
        testConflictSolution.addConflictingAction(lichtOnAction);

        testConflictSolution.snoozeAction(lichtOffAction, true);
        testConflictSolution.snoozeAction(lichtOnAction, true);

        testConflictSolution.addSolvingAction(new LightOnAction("Custom solution action", InconsistencyDevices.LIVING_SPOTS, Color.GREEN, true));

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