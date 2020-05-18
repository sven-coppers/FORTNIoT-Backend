package sven.phd.iot.students.mathias;

import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionsManager {
    private Map<String, Action> actions;
    private Map<String, List<String>> actionExecutions;

    public ActionsManager() {
        System.out.println("ActionsManager - Initiating...");
        this.actions = new HashMap<>();
        this.actionExecutions = new HashMap<>();
    }

    public Action getAction(String actionID) {
        return this.actions.get(actionID);
    }

    public Map<String, Action> getActions() {
        return this.actions;
    }

    public void addAction(Action action) {
        this.actions.put(action.id, action);
    }

    public void deleteAction(String actionID) {
        this.actions.remove(actionID);
    }

    public void clearActions() {
        this.actions.clear();
    }

    public void addActionExecution(String actionID, String contextID) {
        List<String> executions = actionExecutions.get(actionID);
        if (executions == null) {
            executions = new ArrayList<>();
        }
        if (!executions.contains(contextID)) {
            executions.add(contextID);
        }
        actionExecutions.put(actionID, executions);
    }

    public void removeActionExecution(String actionID, String contextID) {
        List<String> executions = actionExecutions.get(actionID);
        if (executions != null) {
            executions.remove(contextID);
            actionExecutions.put(actionID, executions);
        }
    }

    public Action getActionByContextID(String contextID) {
        for (String actionID: actions.keySet()) {
            List<String> executions = actionExecutions.get(actionID);
            if (executions != null && executions.contains(contextID)) {
                return actions.get(actionID);
            }
        }

        return null;
    }
}
