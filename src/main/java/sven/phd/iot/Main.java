package sven.phd.iot;

import sven.phd.iot.students.bram.questions.why.user.UserService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.logging.Logger;

public class Main implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Main - Server Started");

        ContextManager contextManager = ContextManager.getInstance(); // Start the ContextManager
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Main - Server stopped");
    }
}