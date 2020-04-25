package sven.phd.iot.students.bram.rules;

import sven.phd.iot.rules.Action;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.LightOffAction;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.students.bram.rules.actions.OutletOffAction;
import sven.phd.iot.students.bram.rules.actions.OutletOnAction;
import sven.phd.iot.students.bram.rules.triggers.ANDTrigger;

//import java.awt.*;
import java.awt.*;
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
        Action lampOnAction = new OutletOnAction("switch.lamp", "turn on the lamp");
        Action lampOffAction = new OutletOffAction("switch.lamp");

        List<Trigger> andTriggers = new ArrayList<>();
        andTriggers.add(lampOffTrigger);
        andTriggers.add(voetenZakOffTrigger);

        Trigger andTest = new ANDTrigger("rule.and_test", "ANDTRigger", andTriggers);
        andTest.addAction(lampOnAction);

        //rules.put("rule.and_test", andTest);

        //Sync lamp with sun
        Trigger sunSetTrigger = new StateTrigger("rule.sun_set", "sun.sun", "below_horizon", "IF sun set");
        sunSetTrigger.addAction(lampOnAction);
        rules.put("rule.sun_set", sunSetTrigger);

        Trigger sunRiseTrigger = new StateTrigger("rule.sun_rise", "sun.sun", "above_horizon", "IF sun rise");
        sunRiseTrigger.addAction(lampOffAction);
        rules.put("rule.sun_rise", sunRiseTrigger);

        return rules;
    }
}
