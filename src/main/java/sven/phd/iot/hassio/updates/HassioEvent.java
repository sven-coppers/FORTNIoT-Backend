package sven.phd.iot.hassio.updates;

import java.util.Date;

public class HassioEvent extends HassioUpdate {
    public HassioEvent(String entityID, Date datetime) {
        super(entityID, "HassioEvent", datetime);
    }
}
