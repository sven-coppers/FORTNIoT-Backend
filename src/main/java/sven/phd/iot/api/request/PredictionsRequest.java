package sven.phd.iot.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PredictionsRequest {
    @JsonProperty("predictions") public boolean predictions;
    @JsonProperty("tick_interval_minutes") public long tickRate;
    @JsonProperty("tick_window_minutes") public long window;

    public PredictionsRequest() {
        tickRate = -1;
        window = -1;
    }
}
