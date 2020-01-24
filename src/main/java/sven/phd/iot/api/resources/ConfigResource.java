package sven.phd.iot.api.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.models.Configuration;
import sven.phd.iot.api.request.RuleUpdateRequest;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("config/")
public class ConfigResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Configuration getConfig() {
        Configuration config = new Configuration();
        config.listeningToHassio = ContextManager.getInstance().getHassioDeviceManager().isListeningToHassioInstance();

        return config;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void setConfig(Configuration config)  {
        HassioDeviceManager hassioDeviceManager = ContextManager.getInstance().getHassioDeviceManager();

        // If we are listing, but we shouldn't
        if(hassioDeviceManager.isListeningToHassioInstance() && !config.listeningToHassio) hassioDeviceManager.stopListening();

        // If we should listen, but aren't
        if(!hassioDeviceManager.isListeningToHassioInstance() && config.listeningToHassio) hassioDeviceManager.startListening();
    }
}
