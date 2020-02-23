package sven.phd.iot.students.bram.questions.why;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.states.HassioAbstractState;
import sven.phd.iot.students.bram.questions.why.user.HassioUser;
import sven.phd.iot.students.bram.questions.why.user.UserService;

public class WhyQuestion {
    public static boolean stateBecauseOfRule(String deviceId) {
        HassioAbstractState state = ContextManager.getInstance().getHassioState(deviceId);
        return state.context.user_id == null;
    }

    /**
     * Get the user that has triggered a change on a device
     * @return
     */
    public static HassioUser getUserActor(String deviceId) {
        HassioAbstractState state = ContextManager.getInstance().getHassioState(deviceId);
        String userId = state.context.user_id;
        return UserService.getInstance().getUser(userId);

    }

    public static String getActorID(String deviceID) {
        HassioAbstractState state = ContextManager.getInstance().getHassioState(deviceID);
        String userId = state.context.user_id;
        return userId;
    }


}
