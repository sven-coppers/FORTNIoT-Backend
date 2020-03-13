package sven.phd.iot.study.cases;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.rules.Trigger;
import sven.phd.iot.rules.actions.LightOffAction;
import sven.phd.iot.rules.actions.LightOnAction;
import sven.phd.iot.rules.actions.StateAction;
import sven.phd.iot.rules.triggers.PeopleHomeTrigger;
import sven.phd.iot.rules.triggers.StateTrigger;
import sven.phd.iot.rules.triggers.TVGuideTrigger;
import sven.phd.iot.students.bram.rules.triggers.ANDTrigger;
import sven.phd.iot.study.StudyRuleSet;

import java.awt.*;

public class TVRules extends StudyRuleSet {
    @Override
    public void createRules(RulesManager rulesManager) {
        Trigger nobodyHomeTrigger = new PeopleHomeTrigger("rule.tv_nobody_home", false);
        nobodyHomeTrigger.addAction(new StateAction("turn off tv", TVDevices.LIVING_TV, "off"));
        nobodyHomeTrigger.addAction(new LightOffAction("turn off led strip", LightDevices.LIVING_LED_STRIPS));
        rulesManager.addRule(nobodyHomeTrigger);

        ANDTrigger movieTrigger = new ANDTrigger("rule.tv_movie_started");
        movieTrigger.addTrigger(new StateTrigger("", TVDevices.LIVING_TV, "on", "tv is on"));
        movieTrigger.addTrigger(new TVGuideTrigger("", "movie starts", TVDevices.LIVING_TV_GUIDE, null, "movie"));
        movieTrigger.addAction(new LightOffAction("turn off chandelier", LightDevices.LIVING_CHANDELIER));
        movieTrigger.addAction(new LightOnAction("turn on led strip", LightDevices.LIVING_LED_STRIPS, Color.ORANGE, false));
        rulesManager.addRule(movieTrigger);

        ANDTrigger sportsTrigger = new ANDTrigger("rule.tv_sports_started");
        sportsTrigger.addTrigger(new StateTrigger("", TVDevices.LIVING_TV, "on", "tv is on"));
        sportsTrigger.addTrigger(new TVGuideTrigger("", "playing sports", TVDevices.LIVING_TV_GUIDE, null, "sports"));
        sportsTrigger.addAction(new LightOnAction("turn on led strip", LightDevices.LIVING_LED_STRIPS, Color.GREEN, false));
        rulesManager.addRule(sportsTrigger);

        ANDTrigger serieTrigger = new ANDTrigger("rule.tv_serie_started");
        serieTrigger.addTrigger(new StateTrigger("", TVDevices.LIVING_TV, "on", "tv is on"));
        serieTrigger.addTrigger(new TVGuideTrigger("", "watching a series", TVDevices.LIVING_TV_GUIDE, null, "serie"));
        serieTrigger.addAction(new LightOffAction("turn off led strip", LightDevices.LIVING_LED_STRIPS));
        rulesManager.addRule(serieTrigger);

        Trigger newsTrigger = new TVGuideTrigger("rule.tv_news_started", "the news is on", TVDevices.LIVING_TV_GUIDE, null, "news");
        newsTrigger.addAction(new StateAction("turn on tv", TVDevices.LIVING_TV, "on"));
        rulesManager.addRule(newsTrigger);

        Trigger sleepingTrigger = new StateTrigger("rule.sleeping_tv_off", RoutineDevices.ROUTINE, "sleeping", "sleeping");
        sleepingTrigger.addAction(new StateAction("turn off tv", TVDevices.LIVING_TV, "off"));
        sleepingTrigger.addAction(new LightOffAction("turn off tv", LightDevices.LIVING_LED_STRIPS));
        rulesManager.addRule(sleepingTrigger);
    }
}
