package sven.phd.iot.students.mathias;

import sven.phd.iot.ContextManager;
import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.students.mathias.states.HassioConflictSolutionActionState;
import sven.phd.iot.students.mathias.states.HassioConflictSolutionState;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConflictSolver {
    private static ConflictSolver conflictSolver = null;

    private List<HassioConflictSolutionState> _conflicSolutions;

    private ConflictSolver(){
        _conflicSolutions = new ArrayList<>();
    }

    public static ConflictSolver getInstance() {
        if (conflictSolver == null) {
            conflictSolver = new ConflictSolver();
        }

        return conflictSolver;
    }

    public List<HassioConflictSolutionState> getConflictSolutions() {
        return _conflicSolutions;
    }

    /**
     * This function checks if there are any changes to the conflict solutions and updates the attached actions
     * TODO: fill in
     */
    public void updateConflictSolver(){

    }

    public boolean addSolution(HassioConflictSolutionState solution) {
        boolean success = false;
        for (HassioConflictSolutionActionState action: solution.actions) {
            if (action.type.equals("MUTE")) {
                String id = action.action_id;
                Date time = action.action_time;

                Trigger rule = ContextManager.getInstance().getRule(id);
                if (rule != null) {
                    rule.startTimeMute = time;
                }
                success = true;
            } else if (action.type.equals("SELECT")) {
                // TODO fill in select action
            } else if (action.type.equals("CREATE")) {
                // TODO fill in create action
            }
        }
        _conflicSolutions.add(solution);

        return success;
    }
}
