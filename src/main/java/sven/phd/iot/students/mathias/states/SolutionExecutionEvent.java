package sven.phd.iot.students.mathias.states;

import sven.phd.iot.hassio.updates.ExecutionEvent;

import java.util.Date;

public class SolutionExecutionEvent extends ExecutionEvent {
    public SolutionExecutionEvent(String solutionID, Date datetime) {
        super(solutionID, datetime);
    }
}
