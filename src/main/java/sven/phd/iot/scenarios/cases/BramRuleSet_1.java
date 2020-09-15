package sven.phd.iot.scenarios.cases;

import sven.phd.iot.rules.RuleExecution;
import sven.phd.iot.rules.Action;
import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.LightOffAction;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.scenarios.RuleSet;
import sven.phd.iot.students.bram.rules.triggers.ANDTrigger;
import sven.phd.iot.students.mathias.ActionExecutions;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BramRuleSet_1 extends RuleSet {
    @Override
    public void createRules(RulesManager rulesManager, ActionExecutions actionsManager) {
        // When the sun goes below horizon
        Trigger sunSet = new StateTrigger("rule.sun_set", "sun.sun", "below_horizon", "When the sun goes below horizon");
        //When anyone is home
        Trigger anyoneHome = new PeopleHomeTrigger("rule.anyone_home", true);
        // AND trigger
        ANDTrigger sunSetAndHome = new ANDTrigger("rule.lamp_on");
        sunSetAndHome.addTrigger(sunSet);
        sunSetAndHome.addTrigger(anyoneHome);
        // Turn on the lights
        Action tableLightOn = new LightOnAction("turn on the table lights", BramDeviceSet_1.TABLE_LIGHTS, Color.YELLOW, false);
        sunSetAndHome.addAction(tableLightOn);
        //Add execution yesterday at 7pm
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        cal.setTime(new Date(getTime(19,00)));

        sunSetAndHome.addExecution(new RuleExecution(cal.getTime(), sunSetAndHome.id, null));
        rulesManager.addRule(sunSetAndHome);

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
