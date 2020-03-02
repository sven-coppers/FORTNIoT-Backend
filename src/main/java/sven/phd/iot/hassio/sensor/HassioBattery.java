package sven.phd.iot.hassio.sensor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import sven.phd.iot.hassio.states.HassioAttributes;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioEvent;

import java.io.IOException;
import java.util.*;

public class HassioBattery extends HassioSensor {
    private final double minutesPerProcent; // minutes per percent

    public HassioBattery(String entityID, String friendlyName, double minutesPerProcent) {
        super(entityID, friendlyName);
        this.minutesPerProcent = minutesPerProcent;
    }

    @Override
    public HassioAttributes processRawAttributes(JsonNode rawAttributes) throws IOException {
        return new ObjectMapper().readValue(rawAttributes.toString(), HassioBatteryAttributes.class);
    }

   /* @Override
    protected List<HassioState> adaptStateToContext(Date newDate, HashMap<String, HassioState> hassioStates) {
        List<HassioState> result = new ArrayList<>();

        HassioState lastState = hassioStates.get(this.entityID);

        if(lastState == null) return result;

        double lastLevel = Double.parseDouble(lastState.state);
        Long deltaTimeInMilliseconds = newDate.getTime() - lastState.last_changed.getTime();
        double deltaTimeInHours = ((double) deltaTimeInMilliseconds) / (1000.0 * 60.0 * 60.0);
        double newLevel = lastLevel - Math.abs(this.depletionRate) * deltaTimeInHours;

        result.add(new HassioState(this.entityID, "" + newLevel, newDate, new HassioBatteryAttributes()));

        return result;
    } */

    @Override
    public List<HassioState> getFutureStates() {
        List<HassioState> result = new ArrayList<>();
        HassioState lastState = this.getLastState();

        if(lastState == null) return result;

        double lastLevel = Double.parseDouble(lastState.state);
        Date lastDate = lastState.getLastChanged();
        Calendar relativeTime = Calendar.getInstance();
        relativeTime.setTime(lastDate);
        double predictionRate = 2.5; // every 2.5%

        for(double newLevel = lastLevel; newLevel > 0.0; newLevel -= predictionRate) {
            relativeTime.add(Calendar.MINUTE, (int) (this.minutesPerProcent * predictionRate));

            result.add(new HassioState(this.entityID, "" + newLevel, relativeTime.getTime(), new HassioBatteryAttributes()));
        }

        return result;
    }
}
