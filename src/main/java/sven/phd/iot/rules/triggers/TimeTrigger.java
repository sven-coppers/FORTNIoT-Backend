package sven.phd.iot.rules.triggers;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TimeTrigger extends Trigger {
    public enum REPEATING {DAILY, WEEKLY, MONTHLY, YEARLY, NONE};

    private Date recurringStartDate; // Absolute date
    private int frequencyMultiplier;
    private REPEATING repeating;
    private Date relativeStartTime;
    private Date relativeEndTime;

    public TimeTrigger(String ruleIdentifier) {
        super(ruleIdentifier, "Each day...");

        this.recurringStartDate = null;
    }

    @Override
    public boolean isTriggeredBy(HassioChange hassioChange) {
        return true;
    }

    @Override
    public List<HassioContext> verifyCondition(HashMap<String, HassioState> hassioStates) {

        List<HassioContext> triggerDates = new ArrayList<>();

        return triggerDates;
    }
}