package sven.phd.iot.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class Event {
    public enum ACTION {PRESSED, SUN_SET, SUN_RISE};
    public enum CLIENT {WEB_CLIENT, HASSIO, SYSTEM, PHYSICAL, EXTERNAL}
    public enum USER {USER, SYSTEM}

    public String identifier;
    public ACTION action;
    public CLIENT client;
    public USER user;
    public List<String> contexts;

    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    Date timestamp;
}
