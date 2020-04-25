package sven.phd.iot.api.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.api.request.PredictionsRequest;
import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.api.request.ConnectionRequest;
import sven.phd.iot.predictions.PredictionEngine;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("config/")
public class ConfigResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ConnectionRequest getConfig() {
        ConnectionRequest config = new ConnectionRequest();
        config.connectedToHassio = ContextManager.getInstance().getHassioDeviceManager().isConnectedToHassioInstance();

        return config;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void setConfig(ConnectionRequest config)  {
        HassioDeviceManager hassioDeviceManager = ContextManager.getInstance().getHassioDeviceManager();

        // If we are listing, but we shouldn't
        if(hassioDeviceManager.isConnectedToHassioInstance() && !config.connectedToHassio) hassioDeviceManager.stopListening();

        // If we should listen, but aren't
        if(!hassioDeviceManager.isConnectedToHassioInstance() && config.connectedToHassio) hassioDeviceManager.startListening();
    }

    @GET
    @Path("predictions/")
    @Produces(MediaType.APPLICATION_JSON)
    public PredictionsRequest getPredictionsConfig() {
        PredictionsRequest config = new PredictionsRequest();
        config.predictions = ContextManager.getInstance().getPredictionEngine().isPredicting();

        return config;
    }

    @PUT
    @Path("predictions/")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setConfig(PredictionsRequest config)  {
        PredictionEngine predictionEngine = ContextManager.getInstance().getPredictionEngine();

        // If we are listing, but we shouldn't
        if(predictionEngine.isPredicting() && !config.predictions) predictionEngine.stopFuturePredictions();

        // If we should listen, but aren't
        if(!predictionEngine.isPredicting() && config.predictions) predictionEngine.startFuturePredictions();
        StateResource.getInstance().broadcastRefresh();
    }

    @GET
    @Path("predictions/update")
    public void updatePredictions() {
        ContextManager.getInstance().getPredictionEngine().updateFuturePredictions();
        StateResource.getInstance().broadcastRefresh();
    }
}
