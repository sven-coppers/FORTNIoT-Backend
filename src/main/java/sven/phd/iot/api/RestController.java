package sven.phd.iot.api;

import org.glassfish.jersey.server.ResourceConfig;
import sven.phd.iot.api.resources.EventResource;
import sven.phd.iot.api.resources.RuleResource;
import sven.phd.iot.api.resources.StateResource;
import sven.phd.iot.api.resources.ConfigResource;
import sven.phd.iot.students.bram.BramResource;
import sven.phd.iot.students.bram.DeviceResource;
import sven.phd.iot.students.mathias.MathiasResource;

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

        // Resources Mathias
        resources.add(MathiasResource.class);

        //Resources Bram
        resources.add(BramResource.class);
        resources.add(DeviceResource.class);

        return resources;
    }
}

