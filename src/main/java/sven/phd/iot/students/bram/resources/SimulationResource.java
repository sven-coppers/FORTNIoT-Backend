package sven.phd.iot.students.bram.resources;


import org.json.simple.JSONObject;
//import org.reflections.Reflections;
import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;
import sven.phd.iot.predictions.Future;
import sven.phd.iot.rules.Trigger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.*;

@Path("/bram/simulation")
public class SimulationResource {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Future simulateState(HassioStateRaw stateRaw) {
    //public Future simulateState(String str) {

           /* JSONParser parser = new JSONParser();
            JSONObject obj;
            try {
                obj = (JSONObject) parser.parse(str);
            } catch (Exception err) {
                System.out.println(err);
                return null;
            }

            HassioState state =  jsonToState(obj); */
            HassioDeviceManager hassioDeviceManager = ContextManager.getInstance().getHassioDeviceManager();
            HassioState state = hassioDeviceManager.getDevice(stateRaw.entity_id).processRawState(stateRaw);

            List<HassioState> stateList = new ArrayList<>();
            stateList.add(state);
            HashMap<String, Boolean> rules = makeEnabledRulesList();
            System.out.println("Made rules list");

           // return ContextManager.getInstance().simulateAlternativeFuture(rules, stateList);

            return null;

    }
    private HashMap<String, Boolean> makeEnabledRulesList() {
        Map<String, Trigger> rules = ContextManager.getInstance().getRules();
        HashMap<String, Boolean> enabled = new HashMap<>();
        for(String key : rules.keySet()) {
            enabled.put(key, true);
        }
        return enabled;
    }
    private List<HassioState> makeStateList(HassioState simulatedState) {
        ContextManager c = ContextManager.getInstance();

        Map<String, HassioState> original = c.getHassioStates();
        List<HassioState> states = new ArrayList<>();
        for(String key : original.keySet()) {
            if(key.compareTo(simulatedState.entity_id) == 0) {
                states.add(simulatedState);
            } else {
                states.add(original.get(key));
            }
        }
        return states;
    }

    public static Date parse( String input ) throws java.text.ParseException {

        //NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
        //things a bit.  Before we go on we have to repair this.
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" );

        //this is zero time so we need to add that TZ indicator for
        if ( input.endsWith( "Z" ) ) {
            input = input.substring( 0, input.length() - 1) + "GMT-00:00";
        } else {
            int inset = 6;

            String s0 = input.substring( 0, input.length() - inset );
            String s1 = input.substring( input.length() - inset, input.length() );

            input = s0 + "GMT" + s1;
        }

        return df.parse( input );

    }

}
