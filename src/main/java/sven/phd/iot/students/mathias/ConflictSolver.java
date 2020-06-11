package sven.phd.iot.students.mathias;

import sven.phd.iot.students.mathias.states.ConflictSolution;

import java.util.ArrayList;
import java.util.List;

public class ConflictSolver {
    private static ConflictSolver conflictSolver = null;

    private List<ConflictSolution> _conflicSolutions;

    private ConflictSolver(){
        _conflicSolutions = new ArrayList<>();
    }

    public static ConflictSolver getInstance() {
        if (conflictSolver == null) {
            conflictSolver = new ConflictSolver();
        }

        return conflictSolver;
    }

    public List<ConflictSolution> getConflictSolutions() {
        return _conflicSolutions;
    }

    /**
     * This function checks if there are any changes to the conflict solutions and updates the attached actions
     * TODO: fill in
     */
    public void updateConflictSolver(){

    }

    public boolean addSolution(ConflictSolution solution) {
   /*     boolean success = false;
        for (HassioConflictSolutionActionState action: solution.actions) {
            if (action.type.equals("MUTE RULE")) {
                String ruleID = action.values.ruleID;

                Trigger rule = ContextManager.getInstance().getRule(ruleID);


                success = true;
            } else if (action.type.equals("MUTE ACTION")) {
                String ruleID = action.values.ruleID;
                String actionID = action.values.actionID;

                // TODO rewrite this when there is a list of all actions in the system
                // At the moment this is rule based
                if (!ruleID.isEmpty()) {
                    Trigger rule = ContextManager.getInstance().getRule(ruleID);
                    if (rule != null) {
                        List<Action> ruleActions = rule.actions;
                        for (int i = 0; i < ruleActions.size(); i++) {
                            Action ruleAction = ruleActions.get(i);
                            if (ruleAction.id.equals(actionID)) {
                                System.out.println("Rule: " + rule.id + ", with action (description): " + ruleAction.description + " is muted with time " + ruleAction.startTimeDisable.toString());
                            }
                        }
                    }
                }

                success = true;
            } else if (action.type.equals("CREATE ACTION")) {
                // TODO fill in create action
                System.out.println("Create actions");

                String actionID = action.values.actionID;
                String description = action.values.description;
                String entity_id = action.entity_id;
                HashMap<String, String> attributes = action.values.attributes;

                switch (actionID) {
                    case "LightOnAction":
                        String[] colorValues = attributes.get("color").split("[\\(||\\)||,]");
                        Color color = new Color(Float.parseFloat(colorValues[1]), Float.parseFloat(colorValues[2]), Float.parseFloat(colorValues[3]), Float.parseFloat(colorValues[4]));
                        LightOnAction lightOnAction = new LightOnAction(description, entity_id, color, true);
                        List<HassioState> lightOnState = lightOnAction.simulate(new HassioRuleExecutionEvent("", datetime), null);
                        if (!lightOnState.isEmpty()) {
                            ContextManager.getInstance().getActionsManager().addAction(lightOnAction);
                            ContextManager.getInstance().getActionsManager().addActionExecution(lightOnAction.id, lightOnState.get(0).context.id);
                            ContextManager.getInstance().getHassioDeviceManager().getStateScheduler().scheduleState(lightOnState.get(0));
                        }
                        break;
                    case "LightOffAction":
                        LightOffAction lightOffAction = new LightOffAction(description, entity_id);
                        List<HassioState> lightOffState = lightOffAction.simulate(new HassioRuleExecutionEvent("", datetime), null);
                        if (!lightOffState.isEmpty()) {
                            ContextManager.getInstance().getActionsManager().addAction(lightOffAction);
                            ContextManager.getInstance().getActionsManager().addActionExecution(lightOffAction.id, lightOffState.get(0).context.id);
                            ContextManager.getInstance().getHassioDeviceManager().getStateScheduler().scheduleState(lightOffState.get(0));
                        }
                        break;
                    default:
                        // do nothing
                }

                success = true;
            }
        }

        // Check if a solution already exists
        boolean addedToExistingConflict = false;
        for (HassioConflictSolutionState conflictSolution: _conflicSolutions) {
            if (conflictSolution.entity_id.equals(solution.entity_id)) == 0) {
                addedToExistingConflict = true;

                // If solutions already exists, check if actions on same device already exist and change to the new actions
                // TODO not only remove from list, but also revert previous action
                for (HassioConflictSolutionActionState myNewAction: solution.actions) {
                    conflictSolution.actions.removeIf(conflictAction -> myNewAction.entity_id.equals(conflictAction.entity_id));
                }
                
                conflictSolution.actions.addAll(solution.actions);
            }
        }

        if (!addedToExistingConflict) {
            _conflicSolutions.add(solution);
        }
 */
        return true;
    }
}
