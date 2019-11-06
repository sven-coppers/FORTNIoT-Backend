package sven.phd.iot.hassio.light;

import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.states.HassioContext;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;
import sven.phd.iot.hassio.updates.HassioEvent;

import java.util.ArrayList;
import java.util.List;

public class HassioLight extends HassioDevice {
    public HassioLight(String entityID) {
        super(entityID);
    }
   /* public HassioLight() {
        // This served as a test
        HassioState hassioState = new HassioState();
        hassioState.setEntity_id("hassioState.hue_color_lamp_1");

        Client client = ClientBuilder.newClient();

        WebTarget webTarget = client.target("http://hassio.local:8123/api/services/");

        WebTarget employeeWebTarget = webTarget.path("light/turn_on");

        Invocation.Builder invocationBuilder = employeeWebTarget.request(MediaType.APPLICATION_JSON);

        invocationBuilder.header("x-ha-access", "test1234");

        //Response response = invocationBuilder.post(Entity.entity(hassioState, MediaType.APPLICATION_JSON));
        Response response = invocationBuilder.post(Entity.json("{ \"entity_id\": \"light.hue_color_lamp_1\"}")); // Also sets header "Content-Type": "application/json"

        System.out.println(response.getStatus());
        System.out.println(response.readEntity(String.class));
    }*/

    public HassioState processRawState(HassioStateRaw hassioStateRaw) {
        return new HassioLightState(hassioStateRaw);
    }

  /*  public static List<String> turnOn(HassioLightState hassioLightState) {
        Client client = ClientBuilder.newClient();

        WebTarget webTarget = client.target("http://hassio.local:8123/api/services/");
        WebTarget employeeWebTarget = webTarget.path("light/turn_on");
        Invocation.Builder invocationBuilder = employeeWebTarget.request(MediaType.APPLICATION_JSON);
        invocationBuilder.header("x-ha-access", "test1234");
        invocationBuilder.header("Content-Type", "application/json");

        Gson gson = new Gson();
        String json = gson.toJson(hassioLightState.attributes);
        JSONObject jsonObject = new JSONObject (json);
        jsonObject.put("entity_id", hassioLightState.entity_id);
        jsonObject.remove("friendly_name");
        jsonObject.remove("max_mireds");
        jsonObject.remove("min_mireds");
        jsonObject.remove("supported_features");
        jsonObject.remove("effect_list");


        // this.currentState["state"] = "on";
        // this.currentState["attributes"]["brightness"] = 254;


        // System.out.println(jsonObject);

        Response response = invocationBuilder.post(Entity.entity(jsonObject.toString(), MediaType.APPLICATION_JSON));
        // TODO: return the context

        //System.out.println(response.getStatus());
        //System.out.println(response.readEntity(String.class));

        List<HassioStateRaw> hassioStates = response.readEntity(new GenericType<List<HassioStateRaw>>() {});
    //    List<HassioState> preciseStates = HassioClient.processRawStates(hassioStates);

        List<String> contexts = new ArrayList<String>();

     //   for(HassioState hassioState : preciseStates) {
      //      contexts.add(hassioState.getContext().getId());
     //   }

        return contexts;
    }

    public static List<String> turnOff(HassioLightState hassioLightState) {
        Client client = ClientBuilder.newClient();

        WebTarget webTarget = client.target("http://hassio.local:8123/api/services/");
        WebTarget employeeWebTarget = webTarget.path("light/turn_off");
        Invocation.Builder invocationBuilder = employeeWebTarget.request(MediaType.APPLICATION_JSON);
        invocationBuilder.header("x-ha-access", "test1234");
        invocationBuilder.header("Content-Type", "application/json");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("entity_id", hassioLightState.entity_id);

    //    System.out.println(jsonObject);

        Response response = invocationBuilder.post(Entity.entity(jsonObject.toString(), MediaType.APPLICATION_JSON));


    //    System.out.println(response.getStatus());
    //    System.out.println(response.readEntity(String.class));


      //  System.out.println(response.readEntity(String.class));

        List<HassioStateRaw> hassioStates = response.readEntity(new GenericType<List<HassioStateRaw>>() {});
       // List<HassioState> preciseStates = HassioClient.processRawStates(hassioStates);

        List<String> contexts = new ArrayList<String>();

      //  for(HassioState hassioState : preciseStates) {
     //       contexts.add(hassioState.getContext().getId());
     //   }

        return contexts;
    } */

    public List<HassioContext> setState(HassioState hassioState) {
        if(hassioState.state.equals("on")) {
            return this.callService("light/turn_on", new HassioLightServiceOn((HassioLightState) hassioState));
        } else {
            return this.callService("light/turn_off", new HassioLightServiceOff((HassioLightState) hassioState));
        }
    }

    @Override
    public String getFriendlyName() {
        HassioLightState state = (HassioLightState) this.getLastState();
        return state.attributes.friendly_name;
    }

    @Override
    public List<HassioState> predictFutureStates() {
        // A lamp cannot know its future state
        return new ArrayList<HassioState>();
    }

    @Override
    public List<HassioEvent> predictFutureEvents() {
        // A lamp cannot know its future events
        return new ArrayList<HassioEvent>();
    }
}