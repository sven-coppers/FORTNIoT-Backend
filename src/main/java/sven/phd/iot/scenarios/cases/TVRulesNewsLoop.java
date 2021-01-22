package sven.phd.iot.scenarios.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.LightOffAction;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.rules.actions.StateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.rules.triggers.TVGuideTrigger;
import sven.phd.iot.scenarios.RuleSet;
import sven.phd.iot.students.bram.rules.triggers.ANDTrigger;

import java.awt.*;

public class TVRulesNewsLoop extends RuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
        ANDTrigger homeNewsTrigger = new ANDTrigger("rule.home_news");
        homeNewsTrigger.addTrigger(new PeopleHomeTrigger("", true));
        homeNewsTrigger.addTrigger(new TVGuideTrigger("", "the news is on", TVDevices.LIVING_TV_GUIDE, null, "news"));
        homeNewsTrigger.addAction(new StateAction("turn on tv", TVDevices.LIVING_TV, "on"));
        rulesManager.addRule(homeNewsTrigger);

        ANDTrigger tvLightTrigger = new ANDTrigger("rule.tv_light");
        tvLightTrigger.addTrigger(new StateTrigger("", TVDevices.LIVING_TV, "on", "TV turned on"));
        tvLightTrigger.addTrigger(new StateTrigger("", LightSimpleDevices.LIVING_SPOTS, "on", "lights turned on"));
        tvLightTrigger.addAction(new LightOffAction("turn off the lights", LightSimpleDevices.LIVING_SPOTS));
        rulesManager.addRule(tvLightTrigger);

        ANDTrigger homeLightTrigger = new ANDTrigger("rule.home_light");
        homeLightTrigger.addTrigger(new PeopleHomeTrigger("", true));
        homeLightTrigger.addTrigger(new StateTrigger("", LightSimpleDevices.LIVING_SPOTS, "off", "lights turned off"));
        homeLightTrigger.addAction(new LightOnAction("turn on the lights", LightSimpleDevices.LIVING_SPOTS, Color.YELLOW, false));
        rulesManager.addRule(homeLightTrigger);
    }
}
