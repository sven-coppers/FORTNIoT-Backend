package sven.phd.iot.students.bram;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.light.HassioLight;
import sven.phd.iot.hassio.services.HassioService;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;

import java.util.HashMap;
import java.util.Map;

@Path("bram/")
public class BramResource {
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String helloWorld() {
        return "<h1>Hello, Bram!</h1>";
    }


    @Path("devices")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getDeviceByName() {
        Map<String, HassioDevice> devices = ContextManager.getInstance().getHassioDeviceManager().getDevices();

        JSONArray arr = new JSONArray();

        try {

            devices.forEach((id, device) -> {
                JSONObject item = new JSONObject();
                item.put("id", id);
                JSONObject obj = new JSONObject(device);
                Class c = device.getClass();
                if(c == HassioLight.class) {
                    ((HassioLight)device).getLastState()
                }
                HassioState state = device.getLastState();

                item.put("device", obj);

                arr.put(item);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr.toString();
    }

}
