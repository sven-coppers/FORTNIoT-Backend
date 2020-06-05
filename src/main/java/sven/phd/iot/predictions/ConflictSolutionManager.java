package sven.phd.iot.predictions;

import java.util.ArrayList;
import java.util.List;

public class ConflictSolutionManager {
    private List<ConflictSolution> conflictSolutionList;

    public ConflictSolutionManager() {
        this.conflictSolutionList = new ArrayList<>();

        ConflictSolution testConflictSolution = new ConflictSolution();

        this.addSolution(testConflictSolution);
    }

    public void addSolution(ConflictSolution conflictSolution) {
        this.conflictSolutionList.add(conflictSolution);
    }

    public List<ConflictSolution> getSolutions() {
        return this.conflictSolutionList;
    }
}
