package sven.phd.iot.scenarios;

import sven.phd.iot.rules.RulesManager;
import java.util.Calendar;


abstract public class RuleSet {
    abstract public void createRules(RulesManager rulesManager);
    protected static long getTime(int hours, int minutes) {
        return getTime(hours, minutes, 0);
    }
    protected static long getTime(int hours, int minutes, int seconds) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);
        cal.set(Calendar.SECOND, seconds);

        return cal.getTime().getTime();
    }
}
