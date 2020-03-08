package sven.phd.iot.hassio.person;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioAttributes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class HassioPersonAttributes extends HassioAttributes {
    @JsonProperty("editable") public boolean editable;
    @JsonProperty("gps_accuracy") public int gpsAccuracy;
    @JsonProperty("id") public String id;
    @JsonProperty("latitude") public float latitude;
    @JsonProperty("longitude") public float longitude;
    @JsonProperty("source") public String source;
    @JsonProperty("user_id")public String userID;
    @JsonProperty("entity_picture")public String picturePath;
    @JsonProperty("restored") public boolean restored;
    @JsonProperty("supported_features") public int supportedFeatures;


    public HassioPersonAttributes() {
        // Used by the serializer
    }
}