package sven.phd.iot.students.bram.questions.why.rule;

import sven.phd.iot.ContextManager;
import sven.phd.iot.rules.RuleExecution;
import sven.phd.iot.rules.Action;
import sven.phd.iot.rules.Trigger;

import java.util.List;

public class RuleService {
    public static RuleJson getLastRuleByActionDevice(String deviceId) {

        System.out.println(deviceId);
        List<RuleExecution> events =  ContextManager.getInstance().getPastRuleExecutions();

        String lastEventId;
        RuleJson result = null;

        for(int i = events.size()-1; i >= 0; i--) {
            String eventId = events.get(i).execution_id;
            Trigger rule = ContextManager.getInstance().getRuleById(eventId);


            Action action = null; //rule.getActionOnDevice(deviceId);
            if(action == null) {
                return  null;
            }
            RuleJson obj = new RuleJson();
            obj.rule_id = eventId;
            obj.trigger = rule.getTitle();
            obj.action = action.description;

            return obj;
        }
        return null;
    }



}
