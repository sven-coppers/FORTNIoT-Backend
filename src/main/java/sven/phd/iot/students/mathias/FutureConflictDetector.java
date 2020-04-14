package sven.phd.iot.students.mathias;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.students.mathias.states.HassioConflictState;
//import sven.phd.iot.hassio.states.HassioConflictingAttribute;
import sven.phd.iot.students.mathias.states.HassioConflictingRuleState;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.predictions.Future;
import sven.phd.iot.rules.Action;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FutureConflictDetector {
    private List<HassioConflictState> _futureConflicts;

    public FutureConflictDetector() {}

    public List<HassioConflictState> getFutureConflicts(Future future) {
        //List<HassioState> futureStates = ContextManager.getInstance().getStateFuture();
        //List<HassioConflictState> result = new ArrayList<>();

        /**
         * Find triggers that trigger multiple rules, which effect the same device
         * Should become more general => for every rule (trigger), check if it effects same device(Identifier)
         * FUTURE: Optimally you would like to look at the action contexts first, where all light actions have the same contextId!!
         *//*
        List<HassioRuleExecutionEvent> futuresRuleExecutions = future.futureExecutions;
        for (int i = 0; i < futuresRuleExecutions.size(); i++) {
            HassioRuleExecutionEvent comparingRule = futuresRuleExecutions.get(i);
            Trigger comparingTrigger = comparingRule.getTrigger();
            for (int j = i + 1; j < futuresRuleExecutions.size(); j++) {
                HassioRuleExecutionEvent rule = futuresRuleExecutions.get(j);
                Trigger trigger = rule.getTrigger();

                findConflictingActions(result, comparingRule, rule, comparingTrigger, trigger);
            }
        }

        return result;*/

        return findRaceConditionsInStates(future);
    }

    /**
     * Find conflicting actions and keep result
     * @param result
     * @param comparingRule, earliest event of both that occurred
     * @param rule, event that is being compared to the comparingRule
     * @param comparingTrigger, rule out of comparing rule
     * @param trigger, rule that is being compared to the comparingTrigger
     */
    private void findConflictingActions(List<HassioConflictState> result, HassioRuleExecutionEvent comparingRule, HassioRuleExecutionEvent rule, Trigger comparingTrigger, Trigger trigger) {
        for (Action comparingAction: comparingTrigger.actions) {
            boolean conflictAlreadyExists = false;
            HassioConflictState comparingActionConflictState = containsConflict(result, comparingAction.getDeviceID(), comparingRule.datetime);
            if (comparingActionConflictState != null) {
                conflictAlreadyExists = true;
            }

            for (Action action: trigger.actions) {
                // Check if there is a conflict (operating on same device)
                if (comparingAction.onSameDevice(action)) {
                    // If not in result => make new HassioConflict state
                    // Else, if result conflict contains both rules => do nothing
                    // Else add rule(s) to result conflict
                    if (!conflictAlreadyExists) {
                        comparingActionConflictState = new HassioConflictState(comparingAction.getDeviceID(), comparingRule.datetime);
                        result.add(comparingActionConflictState);
                        conflictAlreadyExists = true;
                    }
                    if (!containsRule(result, comparingAction.getDeviceID(), comparingRule.datetime, comparingTrigger)) {
                        comparingActionConflictState.rules.add(new HassioConflictingRuleState(comparingTrigger.id, comparingRule.datetime));
                    }
                    if (!containsRule(result, comparingAction.getDeviceID(), comparingRule.datetime, trigger)){
                        comparingActionConflictState.rules.add(new HassioConflictingRuleState(trigger.id, rule.datetime));
                    }
                }
            }
        }
    }

    private HassioConflictState containsConflict(List<HassioConflictState> allConflicts, String deviceID, Date datetime) {
        for (HassioConflictState conflict: allConflicts) {
            if (conflict.alreadyExist(deviceID,datetime)){
                return conflict;
            }
        }
        return null;
    }

    private boolean containsRule(List<HassioConflictState> allConflicts, String deviceID, Date datetime, Trigger rule) {
        for (HassioConflictState conflict: allConflicts) {
            if (conflict.alreadyExist(deviceID,datetime)){
                if (conflict.containsRule(rule.id)){
                    return true;
                }
            }
        }
        return false;
    }

    private List<HassioConflictState> findRaceConditionsInStates(Future future){
        List<HassioState> futureStates = future.getFutureStates();
        List<HassioConflictState> result = new ArrayList<>();

        for (int i = 0; i < futureStates.size(); i++) {
            HassioState comparingState = futureStates.get(i);
            HassioRuleExecutionEvent comparingEvent = findFutureRuleExecutionByActionContext(future, comparingState.context.id, comparingState.getLastUpdated());

            if (comparingEvent != null) {
                for (int j = i + 1; j < futureStates.size(); j++) {
                    HassioState state = futureStates.get(j);
                    // if race condition on same entities -> get future rules that execute them
                    if (state.getLastUpdated().compareTo(comparingState.getLastUpdated()) == 0
                            && state.entity_id.equals(comparingState.entity_id)) {
                        HassioRuleExecutionEvent event = findFutureRuleExecutionByActionContext(future, state.context.id, state.getLastUpdated());

                        if (event != null) {
                            boolean conflictAlreadyExists = false;
                            HassioConflictState comparingActionConflictState = containsConflict(result, comparingState.entity_id, comparingState.getLastUpdated());
                            if (comparingActionConflictState != null) {
                                conflictAlreadyExists = true;
                            }

                            if (!conflictAlreadyExists) {
                                comparingActionConflictState = new HassioConflictState(comparingState.entity_id, comparingEvent.datetime);
                                result.add(comparingActionConflictState);
                                conflictAlreadyExists = true;
                            }
                            if (!containsRule(result, comparingState.entity_id, comparingState.getLastUpdated(), comparingEvent.getTrigger())) {
                                comparingActionConflictState.rules.add(new HassioConflictingRuleState(comparingEvent.getTrigger().id, comparingEvent.datetime));
                            }
                            if (!containsRule(result, comparingState.entity_id, comparingState.getLastUpdated(), event.getTrigger())){
                                comparingActionConflictState.rules.add(new HassioConflictingRuleState(event.getTrigger().id, event.datetime));
                            }
                        }

                    }
                }
            }
        }

        return result;
    }

    private HassioRuleExecutionEvent findFutureRuleExecutionByActionContext(Future future, String contextId, Date datetime) {
        List<HassioRuleExecutionEvent> futuresRuleExecutions = future.futureExecutions;
        for (HassioRuleExecutionEvent event: futuresRuleExecutions) {
            if (event.datetime.compareTo(datetime) == 0) {
                List<HassioContext> actionContexts = event.actionContexts;
                for (HassioContext context: actionContexts) {
                    if (context.id.equals(contextId)) {
                        return event;
                    }
                }
            }
        }

        return null;
    }

    private void findConflictsInRange() {
        /**
         * Find multiple state changes in certain range and emit warning of possible conflict
         *//*
        long threshold = 15 * 60000; // 15 minutes in milliseconds
        for (HassioState comparingState: futureStates) {
            for (HassioState state: futureStates) {
                // see if state.datetime is in range
                Date maxTime =  new Date(comparingState.datetime.getTime() + threshold);
                if (state.entity_id == comparingState.entity_id && state.datetime.compareTo(comparingState.datetime) > 0 && state.datetime.compareTo(maxTime) <= 0) {
                    HassioConflictState conflictState = state.compareAttributes(comparingState);
                    if (conflictState != null) {
                        result.add(conflictState);

                        System.out.println("--WARNING--");
                        System.out.println("Time in conflict: " + conflictState.datetime.toString());
                        System.out.println("Time in state: " + state.datetime.toString());
                        System.out.println("Time in comparingstate: " + comparingState.datetime.toString());
                        System.out.println("Entity: " + conflictState.entity_id);

                        for (HassioConflictingAttribute attribute: conflictState.conflicts) {
                            System.out.println(attribute.attribute_name + ": " + attribute.value1 + "    &   " + attribute.value2);
                        }
                        System.out.println("--------------------------------------------------------------");

                    }
                }
            }
        }
*/
    }
}
