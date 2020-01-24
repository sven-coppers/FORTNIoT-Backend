package sven.phd.iot.api.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.api.request.UseCaseRequest;
import sven.phd.iot.hassio.HassioDeviceManager;
import sven.phd.iot.models.Configuration;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("study/")
public class StudyResource {
    @PUT
    @Path("case/")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setConfig(UseCaseRequest useCaseRequest) {
        System.out.println("Rule set: " + useCaseRequest.ruleSet);
        System.out.println("Device set: " + useCaseRequest.deviceSet);
        System.out.println("State set: " + useCaseRequest.stateSet);

        HassioDeviceManager hassioDeviceManager = ContextManager.getInstance().getHassioDeviceManager();

        if(useCaseRequest.ruleSet.equals("simple")) {

        }
    }
}
