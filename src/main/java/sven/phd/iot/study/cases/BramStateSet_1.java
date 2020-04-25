package sven.phd.iot.study.cases;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.HassioStateScheduler;
import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.light.HassioLight;
import sven.phd.iot.hassio.light.HassioLightAttributes;
import sven.phd.iot.hassio.person.HassioPersonAttributes;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.sun.HassioSunAttributes;
import sven.phd.iot.study.StudyStateSet;

import java.util.Calendar;
import java.util.Date;

public class BramStateSet_1 extends StudyStateSet {
    ContextManager contextManager;

    @Override
    public void setInitialStates(HassioDeviceManager DM, Date startDate) {
        DM.logState(new HassioState(BramDeviceSet_1.PERSON, "home", startDate, PersonAttributes()));

        // Lamp on because of user
        HassioContext context = new HassioContext();
        context.user_id = BramDeviceSet_1.PERSON_ID;
        HassioState tableLightsState = new HassioState(BramDeviceSet_1.TABLE_LIGHTS, "on", startDate, LightAttributes());
        tableLightsState.context = context;
        DM.logState(tableLightsState);
    }


    @Override
    public void scheduleFutureStates(HassioStateScheduler SS, Calendar relativeTime) {
        // Bram leaves
        Date bramLeaves = relativeTime.getTime();
        bramLeaves.setTime(getTime(20,07));
        relativeTime.add(Calendar.MINUTE, 100);
        SS.scheduleState(new HassioState(BramDeviceSet_1.PERSON, "away", relativeTime.getTime(), PersonAttributes()));
    }

    private static HassioSunAttributes SunAttributes() {
        HassioSunAttributes attr = new HassioSunAttributes();
        attr.friendlyName = "Sun";
        return attr;
    }
    private static HassioLightAttributes LightAttributes() {
        HassioLightAttributes attr = new HassioLightAttributes();
        attr.friendlyName = BramDeviceSet_1.TABLE_LIGHTS_NAME;
        return attr;
    }
    private static HassioPersonAttributes PersonAttributes() {
        HassioPersonAttributes attr = new HassioPersonAttributes();
        attr.setEditable(false);
        attr.setUserID(BramDeviceSet_1.PERSON_ID);
        attr.setID(BramDeviceSet_1.PERSON_ID);
        attr.friendlyName = BramDeviceSet_1.PERSON_NAME;
        attr.setGpsAccuracy(5);
        attr.setSource("device.phone");
        return attr;
    }

}
