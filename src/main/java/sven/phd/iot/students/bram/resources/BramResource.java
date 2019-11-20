package sven.phd.iot.students.bram.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.HassioDevice;
import sven.phd.iot.hassio.light.HassioLight;
import sven.phd.iot.hassio.services.HassioService;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.states.HassioStateRaw;
import sven.phd.iot.students.bram.questions.why.WhyQuestion;
import sven.phd.iot.students.bram.questions.why.WhyResult;

import java.util.HashMap;
import java.util.Map;

@Path("bram/")
public class BramResource {
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String helloWorld() {
        return "<h1>Hello, Bram!</h1>";
    }




}
