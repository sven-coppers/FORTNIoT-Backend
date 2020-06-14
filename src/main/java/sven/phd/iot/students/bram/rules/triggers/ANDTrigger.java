package sven.phd.iot.students.bram.rules.triggers;


import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.rules.Trigger;

import java.util.*;

public class ANDTrigger extends Trigger {
    private List<Trigger> triggers;

    public ANDTrigger(String ruleIdentifier, String title, List<Trigger> triggers) {
        super(ruleIdentifier, title);

        this.triggers = triggers;

        makeTitle();
    }

    /**
     * Written by Sven
     * @param ruleIdentifier
     */
    public ANDTrigger(String ruleIdentifier) {
        super(ruleIdentifier, "");
        this.triggers = new ArrayList<>();
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
                this.title += " AND ";
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
    public List<HassioState> verifyCondition(HashMap<String, HassioState> hassioStates) {
        // Verify all individual rules
        List<HassioState> contexts = new ArrayList<>();
        for(Trigger trigger: this.triggers) {
            List<HassioState> context = trigger.verifyCondition(hassioStates);

            // IF child was not triggered
            if(context == null) {
                return null;
            }
            contexts.addAll(context);
        }
        //System.out.println("And rule is triggered");
        return contexts;
    }

    /** Written by Sven */
    public void addTrigger(Trigger trigger) {
        this.triggers.add(trigger);
        this.makeTitle();
    }
    @Override
    public List<String> getTriggeringEntities() {
        List<String> result = new ArrayList<>();
        for(Trigger t:triggers) {
            result.addAll(t.getTriggeringEntities());
        }
        return result;
    }
}
