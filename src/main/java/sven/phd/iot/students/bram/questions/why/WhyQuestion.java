package sven.phd.iot.students.bram.questions.why;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.person.HassioPerson;
import sven.phd.iot.hassio.person.HassioPersonAttributes;
import sven.phd.iot.hassio.states.HassioAbstractState;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.students.bram.questions.why.user.HassioUser;
import sven.phd.iot.students.bram.questions.why.user.UserService;

import java.util.Map;

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

        ContextManager cm = ContextManager.getInstance();
        Map<String, HassioState> devices = cm.getHassioDeviceManager().getCurrentStates();
        for(Map.Entry<String, HassioState> entry : devices.entrySet()) {
            if(!entry.getKey().contains("person"))
                continue;
            HassioPersonAttributes attr = (HassioPersonAttributes) entry.getValue().attributes;
            if(attr.getID().compareTo(userId) == 0) {
                HassioUser user = new HassioUser();
                user.id= userId;
                user.name = attr.friendlyName;
                user.is_active = true;
                user.is_owner = true;
                user.system_generated = false;
                return user;
            }
        }
        return null;

    }

    public static String getActorID(String deviceID) {
        HassioAbstractState state = ContextManager.getInstance().getHassioState(deviceID);
        String userId = state.context.user_id;
        return userId;
    }


}
