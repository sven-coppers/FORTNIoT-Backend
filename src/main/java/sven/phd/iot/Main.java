package sven.phd.iot;

import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.SseFeature;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Feature;

public class Main implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Joepie: de server werd gestart");

        ContextManager contextManager = ContextManager.getInstance(); // Start the ContextManager
    }

    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("Vaarwel: Server stopped");
    }
}