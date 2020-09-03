package sven.phd.iot.api.resources;

import sven.phd.iot.ContextManager;
import sven.phd.iot.api.request.ExperimentRequest;
import sven.phd.iot.api.request.UseCaseRequest;
import sven.phd.iot.conflicts.ConflictVerificationManager;
import sven.phd.iot.scenarios.ScenarioManager;
import sven.phd.iot.study.Task;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Singleton
@Path("study/")
public class StudyResource {
    @GET
    @Path("case/")
    @Produces(MediaType.APPLICATION_JSON)
    public UseCaseRequest getConfig() {
        ScenarioManager scenarioManager = ContextManager.getInstance().getScenarioManager();
        ConflictVerificationManager conflictVerificationManager = ContextManager.getInstance().getConflictVerificationManager();

        UseCaseRequest useCaseRequest = new UseCaseRequest();

        useCaseRequest.deviceSet = scenarioManager.getDeviceSet();
        useCaseRequest.ruleSet = scenarioManager.getRuleSet();
        useCaseRequest.stateSet = scenarioManager.getStateSet();
        useCaseRequest.preset = scenarioManager.getActivePreset();
        useCaseRequest.activeConflictVerifiers = conflictVerificationManager.getActiveVerifiers();

        useCaseRequest.deviceSetOptions = scenarioManager.getDeviceSetOptions();
        useCaseRequest.ruleSetOptions = scenarioManager.getRuleSetOptions();
        useCaseRequest.stateSetOptions = scenarioManager.getStateSetOptions();
        useCaseRequest.presetOptions = scenarioManager.getPresets();
        useCaseRequest.conflictVerifyOptions = conflictVerificationManager.getAllVerifiers();

        return useCaseRequest;
    }

    @PUT
    @Path("case/")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setConfig(UseCaseRequest useCaseRequest) {
        ScenarioManager scenarioManager = ContextManager.getInstance().getScenarioManager();
        ConflictVerificationManager conflictVerificationManager = ContextManager.getInstance().getConflictVerificationManager();

        scenarioManager.setDeviceSet(useCaseRequest.deviceSet);
        scenarioManager.setRuleSet(useCaseRequest.ruleSet);
        scenarioManager.setStateSet(useCaseRequest.stateSet);
        conflictVerificationManager.setActiveVerifiers(useCaseRequest.activeConflictVerifiers);

        if(useCaseRequest.preset != null) {
            scenarioManager.setActivePreset(useCaseRequest.preset);
        }

        ContextManager.getInstance().getPredictionEngine().updateFuturePredictions();

        StateResource.getInstance().broadcastRefresh();
    }

    @PUT
    @Path("start/")
    @Consumes(MediaType.APPLICATION_JSON)
    public void startExeperiment(ExperimentRequest er) {
        ContextManager.getInstance().getStudyManager().start(er.participant, er.group);
    }

    @GET
    @Path("next/")
    public void nextStep() {
        ContextManager.getInstance().getStudyManager().next();
    }

    @GET
    @Path("steps/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Task> getSteps() {
        return ContextManager.getInstance().getStudyManager().getTasks();
    }
}
