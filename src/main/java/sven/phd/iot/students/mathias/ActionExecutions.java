package sven.phd.iot.students.mathias;

import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionExecutions {
    private Map<Action, List<String>> actionExecutions;

    public ActionExecutions() {
        actionExecutions = new HashMap<>();
    }

    public void addActionExecution(Action action, String contextID) {
        List<String> executions = actionExecutions.get(action);
        if (executions == null) {
            executions = new ArrayList<>();
        }
        if (!executions.contains(contextID)) {
            executions.add(contextID);
        }
        actionExecutions.put(action, executions);
    }

    public void removeActionExecution(Action action, String contextID) {
        List<String> executions = actionExecutions.get(action);
        if (executions != null) {
            executions.remove(contextID);
            actionExecutions.put(action, executions);
        }
    }

    public Action getActionByContextID(String contextID) {
        for (Action action: actionExecutions.keySet()) {
            List<String> executions = actionExecutions.get(action);
            if (executions != null && executions.contains(contextID)) {
                return action;
            }
        }

        return null;
    }

    public Map<String, Action> getAllActions() {
        Map<String, Action> result = new HashMap<>();
        for (Action action: actionExecutions.keySet()) {
            result.put(action.id, action);
        }
        return result;
    }
}