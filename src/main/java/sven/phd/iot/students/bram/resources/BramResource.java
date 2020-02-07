package sven.phd.iot.students.bram.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("bram/")
public class BramResource {
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String helloWorld() {
        return "<h1>Hello, Bram!</h1>";
    }




}
