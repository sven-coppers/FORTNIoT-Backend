package sven.phd.iot.students.mathias.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HassioSolutionResponse {
    @JsonProperty("success") public boolean success;
    @JsonProperty("response") public String response;
}
