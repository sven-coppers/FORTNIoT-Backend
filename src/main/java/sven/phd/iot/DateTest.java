package sven.phd.iot;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import sven.phd.iot.hassio.states.HassioDateDeserializer;

import java.util.Date;

public class DateTest {
    @JsonDeserialize(using = HassioDateDeserializer.class)
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
    public Date date;
}
