package sven.phd.iot.api.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.api.request.UseCaseRequest;
import sven.phd.iot.models.StudyManager;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("study/")
public class StudyResource {
    @GET
    @Path("case/")
    @Produces(MediaType.APPLICATION_JSON)
    public UseCaseRequest getConfig() {
        StudyManager studyManager = ContextManager.getInstance().getStudyManager();

        UseCaseRequest useCaseRequest = new UseCaseRequest();

        useCaseRequest.deviceSet = studyManager.getDeviceSet();
        useCaseRequest.ruleSet = studyManager.getRuleSet();
        useCaseRequest.stateSet = studyManager.getStateSet();

        return useCaseRequest;
    }

    @PUT
    @Path("case/")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setConfig(UseCaseRequest useCaseRequest) {
        StudyManager studyManager = ContextManager.getInstance().getStudyManager();

        studyManager.setRuleSet(useCaseRequest.ruleSet);
        studyManager.setDeviceSet(useCaseRequest.deviceSet);
        studyManager.setStateSet(useCaseRequest.stateSet);

        StateResource.getInstance().broadcastRefresh();
    }
}
