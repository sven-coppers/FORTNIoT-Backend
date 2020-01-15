package sven.phd.iot.students.bram.rules.triggers;


import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;
import sven.phd.iot.hassio.weather.HassioWeatherState;
import sven.phd.iot.rules.Trigger;

import java.util.*;

public class ANDTrigger extends Trigger {
    private List<Trigger> triggers;

    public ANDTrigger(String ruleIdentifier, List<Trigger> triggers) {
        super(ruleIdentifier, "ANDTrigger");

        this.triggers = triggers;

        makeTitle();
    }


    /**
     * Make the title of the AND trigger
     * This must be done after initialization of the map
     */
    private void makeTitle() {
        this.title = "";
        int count = this.triggers.size();
        int index = 0;
        for(Trigger trigger : this.triggers) {
            this.title += trigger.getTitle();
            if(index < count - 1) {
                this.title += " and ";
            }
            index++;
        }
    }

    @Override
    public boolean isTriggeredBy(HassioChange hassioChange) {
        for(Trigger trigger: this.triggers) {
            if(trigger.isTriggeredBy(hassioChange)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<HassioContext> verifyCondition(HashMap<String, HassioState> hassioStates) {
        // Verify all individual rules
        List<HassioContext> contexts = new ArrayList<>();
        for(Trigger trigger: this.triggers) {
            List<HassioContext> context = trigger.verifyCondition(hassioStates);
            if(context == null) {
                return null;
            }
            contexts.addAll(context);
        }
        System.out.println("And rule is triggered");
        return contexts;
    }
}
