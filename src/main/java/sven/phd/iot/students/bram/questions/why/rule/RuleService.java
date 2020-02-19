package sven.phd.iot.students.bram.questions.why.rule;

import org.json.JSONObject;
import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.Action;
import sven.phd.iot.rules.Trigger;

import java.util.List;

public class RuleService {
    public static RuleJson getLastRuleByActionDevice(String deviceId) {

        System.out.println(deviceId);
        List<HassioRuleExecutionEvent> events =  ContextManager.getInstance().getPastRuleExecutions();

        String lastEventId;

        for(int i = events.size()-1; i >= 0; i--) {
            String eventId = events.get(i).entity_id;
            Trigger rule = ContextManager.getInstance().getRuleById(eventId);

            Action action = rule.getActionOnDevice(deviceId);
            if(action != null) {
                RuleJson obj = new RuleJson();
                obj.rule_id =  eventId;
                obj.trigger= rule.getTitle();
                obj.action =  action.toString();
                return obj;
            }
        }
        return null;
    }



}