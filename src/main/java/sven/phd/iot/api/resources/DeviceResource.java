package sven.phd.iot.api.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.HassioDevice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;

@Path("/devices")
public class DeviceResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HashMap<String, HassioDevice> getDevices() {
        return ContextManager.getInstance().getHassioDeviceManager().getDevices();
    }
}