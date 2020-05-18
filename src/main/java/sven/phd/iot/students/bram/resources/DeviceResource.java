package sven.phd.iot.students.bram.resources;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.students.bram.HelperFunctions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/bram/devices")
public class DeviceResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getDevices( @QueryParam("search") String search ) {
        if(search == null) {
            return getAllDevices();
        } else {
            return getDeviceByName(search);
        }
    }

    /**
     * Search for a specific device, based on confidence level of similarity
     * @param search
     * @return json array with found devices
     */
    private static String getDeviceByName(String search) {
        search = search.replace("__", " ");
        Map<String, HassioDevice> devices = ContextManager.getInstance().getHassioDeviceManager().getDevices();

        JSONArray arr = new JSONArray();


        try {
            String finalSearch = search;
            for(Map.Entry<String, HassioDevice> entry: devices.entrySet()) {
                if(!entry.getValue().isEnabled())
                    continue;
                JSONObject item = new JSONObject();
                String name = entry.getValue().getFriendlyName();
                //Default similarity is 0
                double similarity = 0;
                //If the device has a friendly name compare it
                if(name != null) {
                    similarity = HelperFunctions.similarity(name, finalSearch);
                }
                //If the similarity is more than 50%
                if(similarity > 0.2) {
                    item.put("id", entry.getKey());
                    item.put("friendly_name", name);
                    item.put("confidence", "" + similarity);
                    arr.put(item);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return HelperFunctions.sortJSONArray(arr, "confidence").toString();
    }

    /**
     * Get the possible types of the existing devices
     * @return
     */
    @GET
    @Path("/types")
    public static String getDeviceTypes() {
        Map<String, HassioDevice> devices = ContextManager.getInstance().getHassioDeviceManager().getDevices();

        List<String> types = new ArrayList<String>();

        for(Map.Entry<String, HassioDevice> entry : devices.entrySet()) {
            if(!entry.getValue().isEnabled()) {
                continue;
            }
            String type = entry.getValue().getClass().getSimpleName();
            type = type.replace("Hassio", "");

            if(!types.contains(type)) {
                types.add(type);
            }
        }

        JSONObject obj = new JSONObject();
        obj.put("types", types);

        return obj.toString();
    }

    /**
     * Get all devices in the system
     * @return
     */
    public static String getAllDevices() {
        Map<String, HassioDevice> devices = ContextManager.getInstance().getHassioDeviceManager().getDevices();

        JSONArray arr = new JSONArray();

        try {
            for(Map.Entry<String, HassioDevice> entry: devices.entrySet()){
                if(!entry.getValue().isEnabled()) {
                    continue;
                }
                JSONObject item = new JSONObject();
                item.put("id", entry.getKey());
                item.put("available", entry.getValue().isAvailable()); // Added by Sven
                item.put("enabled", entry.getValue().isEnabled()); // Added by Sven
                item.put("friendly_name", entry.getValue().getFriendlyName());
                arr.put(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr.toString();
    }
}