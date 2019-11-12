package sven.phd.iot.hassio.light;

import com.fasterxml.jackson.annotation.JsonProperty;
//import com.sun.org.apache.xpath.internal.operations.Bool;
import sven.phd.iot.hassio.services.HassioService;

public class HassioLightService extends HassioService {
    @JsonProperty("transition") public Boolean transition;
}
