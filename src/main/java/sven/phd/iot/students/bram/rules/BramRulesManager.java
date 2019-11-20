package sven.phd.iot.students.bram.rules;

import sven.phd.iot.rules.Action;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.rules.triggers.CalendarBusyTrigger;
import sven.phd.iot.students.bram.rules.actions.OutletOffAction;
import sven.phd.iot.students.bram.rules.actions.OutletOnAction;
import sven.phd.iot.students.bram.rules.triggers.ANDTrigger;
import sven.phd.iot.students.bram.rules.triggers.OutletOffTrigger;
import sven.phd.iot.students.bram.rules.triggers.OutletOnTrigger;

//import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BramRulesManager {
    public static Map<String, Trigger> getRules() {
        Map<String, Trigger> rules= new HashMap<>();

        Trigger lampOffTrigger = new OutletOffTrigger("rule.switch_sync_off", "switch.lamp");
        /*lampOffTrigger.addAction(new OutletOffAction("switch.voetenzak"));
        rules.put("rule.switch_sync_off", lampOffTrigger);

        Trigger lampOnTrigger = new OutletOnTrigger("rule.switch_sync_on", "switch.lamp");
        lampOnTrigger.addAction(new OutletOnAction("switch.voetenzak"));
        rules.put("rule.switch_sync_on", lampOnTrigger);*/


        // AND rule test
        Trigger voetenZakOffTrigger = new OutletOffTrigger("rule.and_rule_2", "switch.voetenzak");
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
