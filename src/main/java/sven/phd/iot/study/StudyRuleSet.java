package sven.phd.iot.study;

import sven.phd.iot.rules.RulesManager;
import sven.phd.iot.students.mathias.ActionsManager;

abstract public class StudyRuleSet {
    abstract public void createRules(RulesManager rulesManager, ActionsManager actionsManager);
}
