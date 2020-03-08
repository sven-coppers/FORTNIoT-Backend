package sven.phd.iot.rules.triggers;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.tv.HassioTVGuideAttributes;
import sven.phd.iot.rules.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TVGuideTrigger extends Trigger {
    private String guideID;
    private String programType;
    private String program;

    public TVGuideTrigger(String id, String title, String guideID, String program, String programType) {
        super(id, title);

        this.program = program;
        this.programType = programType;
        this.guideID = guideID;
    }

    @Override
    public boolean isTriggeredBy(HassioChange hassioChange) {
        return hassioChange.entity_id.equals(guideID);
    }

    @Override
    public List<HassioContext> verifyCondition(HashMap<String, HassioState> hassioStates) {
        HassioState tvguideState = hassioStates.get(guideID);

        if(tvguideState == null) return null;

        String liveProgramType = ((HassioTVGuideAttributes) tvguideState.attributes).contentType;
        ArrayList<HassioContext> result = new ArrayList<>();

        if(this.program != null && this.program.equals(tvguideState.state)) {
            result.add(tvguideState.context);
        } else if(this.programType != null && this.programType.equals(liveProgramType)) {
            result.add(tvguideState.context);
        } else {
            return null;
        }

        return result;
    }

    @Override
    public List<String> getTriggeringEntities() {
        List<String> result = new ArrayList<>();
        result.add(guideID);
        return result;
    }
}
