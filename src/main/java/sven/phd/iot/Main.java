package sven.phd.iot;

import sven.phd.iot.students.bram.questions.why.user.UserService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Main implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Joepie: de server werd gestart");

        ContextManager contextManager = ContextManager.getInstance(); // Start the ContextManager

        //Fetch users from the server
        UserService.getInstance();
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Vaarwel: Server stopped");
    }
}