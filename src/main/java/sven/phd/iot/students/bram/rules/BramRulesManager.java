package sven.phd.iot.students.bram.rules;

import sven.phd.iot.rules.Action;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.students.bram.rules.actions.OutletOnAction;
import sven.phd.iot.students.bram.rules.triggers.ANDTrigger;

//import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BramRulesManager {
    public static Map<String, Trigger> getRules() {
        Map<String, Trigger> rules= new HashMap<>();

        Trigger lampOffTrigger = new StateTrigger("rule.switch_sync_off", "switch.lamp", "off", "");

        // AND rule test
        Trigger voetenZakOffTrigger = new StateTrigger("rule.and_rule_2", "switch.voetenzak", "off", "");
        Action lampOnAction = new OutletOnAction("switch.lamp");

        List<Trigger> andTriggers = new ArrayList<>();
        andTriggers.add(lampOffTrigger);
        andTriggers.add(voetenZakOffTrigger);

        Trigger andTest = new ANDTrigger("rule.and_test", andTriggers);
        andTest.addAction(lampOnAction);

        rules.put("rule.and_test", andTest);

        return rules;
    }
}
