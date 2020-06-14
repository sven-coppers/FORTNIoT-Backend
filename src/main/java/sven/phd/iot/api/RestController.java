package sven.phd.iot.api;

import org.glassfish.jersey.server.ResourceConfig;

import sven.phd.iot.api.resources.*;
import sven.phd.iot.students.bram.resources.*;

import sven.phd.iot.students.mathias.MathiasResource;
import sven.phd.iot.students.mathias.resources.ConflictResource;
import sven.phd.iot.students.mathias.resources.SolutionResource;

import java.util.Set;

public class RestController extends ResourceConfig {
    public RestController(){
        register(CORSFilter.class);
        registerClasses(getResourcesInstance());
    }

    private Set<Class<?>> getResourcesInstance() {
        Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
        Class jsonProvider = null;
        try {
            jsonProvider = Class.forName("org.glassfish.jersey.jackson.JacksonFeature");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        resources.add(jsonProvider);
        resources.add(EventResource.class);
        resources.add(StateResource.class);
        resources.add(RuleResource.class);
        resources.add(ConfigResource.class);
        resources.add(StudyResource.class);
        resources.add(SolutionResource.class);

        // Resources Mathias
        resources.add(MathiasResource.class);
        resources.add(SolutionResource.class);
        resources.add(ConflictResource.class);

        //Resources Bram
        resources.add(BramResource.class);
        resources.add(DeviceResource.class);
        resources.add(WhyResource.class);
        resources.add(SimulationResource.class);
        resources.add(RuleDescriptionResource.class);
        resources.add(WhyNotResource.class);
        return resources;
    }
}

