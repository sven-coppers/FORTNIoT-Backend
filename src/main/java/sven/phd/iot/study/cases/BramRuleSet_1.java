package sven.phd.iot.study.cases;

import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.rules.Action;
import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.LightOffAction;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.study.StudyRuleSet;

import java.awt.*;
import java.util.Calendar;
import java.util.Date;

public class BramRuleSet_1 extends StudyRuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
        // When the sun goes below horizon, turn on the table lights
        Trigger sunSet = new StateTrigger("rule.sun_set", "sun.sun", "below_horizon", "When the sun goes below horizon");
        Action tableLightOn = new LightOnAction("turn on the table lights", BramDeviceSet_1.TABLE_LIGHTS, Color.YELLOW, false);
        sunSet.addAction(tableLightOn);
        //Add execution yesterday at 7pm
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        cal.setTime(new Date(getTime(19,00)));

        sunSet.addExecution(new HassioRuleExecutionEvent(sunSet, cal.getTime(), 0));
        rulesManager.addRule(sunSet);

        // When the sun goes above horizon, turn off the table lights
        Trigger sunRise = new StateTrigger("rule.sun_rise", "sun.sun", "above_horizon", "When the sun goes above horizon");
        sunRise.addAction(new LightOffAction("turn off the table lights", BramDeviceSet_1.TABLE_LIGHTS));
        rulesManager.addRule(sunRise);

        // When Bram leaves the house, turn off the lights
        Trigger personLeaves = new PeopleHomeTrigger("rule.person_leave", false);
        personLeaves.title = "When nobody is home";
        Action tableLightOff = new LightOffAction("turn off table lights", BramDeviceSet_1.TABLE_LIGHTS);
        personLeaves.addAction(tableLightOff);
        rulesManager.addRule(personLeaves);
    }
}
