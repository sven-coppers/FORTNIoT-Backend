package sven.phd.iot.hassio.services;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class HassioService {
    @JsonProperty("entity_id") public String entity_id;
}
