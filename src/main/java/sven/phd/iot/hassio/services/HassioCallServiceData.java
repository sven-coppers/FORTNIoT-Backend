package sven.phd.iot.hassio.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class HassioCallServiceData {
    @JsonProperty("domain") public String domain;
    @JsonProperty("service") public String service;
    @JsonProperty("service_data") public JsonNode serviceData;
}
