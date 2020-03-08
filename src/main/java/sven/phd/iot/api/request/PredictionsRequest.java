package sven.phd.iot.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PredictionsRequest {
    @JsonProperty("predictions") public boolean predictions;
}
