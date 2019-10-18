package sven.phd.iot.rules.triggers;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.weather.HassioWeather;
import sven.phd.iot.hassio.weather.HassioWeatherState;
import sven.phd.iot.rules.Trigger;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class TemperatureTrigger extends Trigger {
    private int min;
    private int max;

    public TemperatureTrigger(String ruleIdentifier, int min, int max) {
        super(ruleIdentifier, "IF temperature between [" + min + ", " + max + "]");

        this.min = min;
        this.max = max;
    }

    @Override
    public boolean isInterested(HassioChange hassioChange) {
        if(hassioChange.entity_id.equals("weather.dark_sky")) {
            float oldTemp = ((HassioWeatherState) hassioChange.hassioChangeData.oldState).attributes.temperature;
            float newTemp = ((HassioWeatherState) hassioChange.hassioChangeData.newState).attributes.temperature;

            if(newTemp <= min || newTemp >= max) {
                return true;
            }

            // If the state
            return oldTemp != newTemp;
        }

        return false;
    }

    @Override
    protected List<HassioContext> verify(HashMap<String, HassioState> hassioStates) {
        HassioWeatherState hassioWeatherState = (HassioWeatherState) hassioStates.get("weather.dark_sky");
        float temperature = hassioWeatherState.attributes.temperature;

        if(temperature > min && temperature < max) {
            List<HassioContext> triggerContexts = new ArrayList<>();
            triggerContexts.add(hassioWeatherState.context);
            return triggerContexts;
        }

        return null;



        // TODO: Create a new date instance, with the same day, but at 9:00
        // TODO: Create a new date instance, with the same day, but at 17:00

        // TODO: Check if the date is between 9:00 and 17:00

        // IDEE: Counterpart regels maken: What to do when the rule is no longer satisfied? (Doe licht terug uit?)
        // NIET DOEN, WANT ALS ER EEN ANDERE REGEL DAARNA DE LAMPEN TERUG WIL AAN DOEN (RACE CONDITIONS?) --> Simulatie wordt verschrikkelijk

     //   Calendar cal = Calendar.getInstance(); // creates calendar
     //   cal.setTime(hassioChange.triggerTime); // sets calendar time/date
     //   cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour

        //    befor
     //   cal.getTime(); // returns new date object, one hour in the future

        // boolean afterNine = .getHours()

     //   return LocalTime.now().isAfter( LocalTime.parse( "09:00" ) ) && oldState.attributes.temperature > min && oldState.attributes.temperature < max;
    }
}
