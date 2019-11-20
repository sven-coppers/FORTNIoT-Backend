package sven.phd.iot.students.bram.questions;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;

public class WhyQuestion {
    public static boolean stateBecauseOfRule(String deviceId) {
        HassioState state = ContextManager.getInstance().getHassioState(deviceId);
        return state.context.user_id != null; 
    }
}
