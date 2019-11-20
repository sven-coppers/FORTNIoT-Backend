package sven.phd.iot;

import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.SseFeature;
import sven.phd.iot.students.bram.questions.why.user.UserService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Feature;

public class Main implements ServletContextListener {
    private Client client;
    private WebTarget target;
    private EventSource eventSource;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Joepie: de server werd gestart");

        ContextManager contextManager = ContextManager.getInstance(); // Start the mainController

        //Fetch users from the server
        UserService.getInstance();

        client = ClientBuilder.newBuilder().register(SseFeature.class).build();
        if(BearerToken.useBearer()) {
            Feature feature = OAuth2ClientSupport.feature(BearerToken.getBearer());
            client.register(feature);
            target = client.target("http://hassio.local:8123/api/stream");

        } else {
            target = client.target("http://hassio.local:8123/api/stream?api_password=test1234");
        }

        eventSource = EventSource.target(target).build();
        eventSource.register(contextManager.getHassioDeviceManager()); // Everything needs to go to a single listener, since Hassio does not support event types

        eventSource.open();
    }

    public void contextDestroyed(ServletContextEvent arg0) {
        eventSource.close();
        System.out.println("Vaarwel: Server stopped");
    }
}