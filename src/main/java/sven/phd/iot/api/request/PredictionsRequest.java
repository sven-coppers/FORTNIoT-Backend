package sven.phd.iot.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PredictionsRequest {
    @JsonProperty("predictions") public boolean predictions;
    @JsonProperty("tick_interval_minutes") public long tickRate;
    @JsonProperty("tick_window_minutes") public long window;
    @JsonProperty("use_case") public String useCase;

    public PredictionsRequest(boolean predictions, long tickRate, long window, String useCase) {
        this.predictions = predictions;
        this.tickRate = tickRate;
        this.window = window;
        this.useCase = useCase;
    }

    public PredictionsRequest() {
        tickRate = -1;
        window = -1;
    }
}
