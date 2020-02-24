package sven.phd.iot.hassio.person;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioAttributes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class HassioPersonAttributes extends HassioAttributes {
    @JsonProperty("editable") boolean editable;
    @JsonProperty("gps_accuracy") int gpsAccuracy;
    @JsonProperty("id") String id;
    @JsonProperty("latitude") float latitude;
    @JsonProperty("longitude") float longitude;
    @JsonProperty("source") String source;
    @JsonProperty("user_id") String userID;
    @JsonProperty("entity_picture") String picturePath;
    @JsonProperty("restored") boolean restored;
    @JsonProperty("supported_features") int supportedFeatures;


    public HassioPersonAttributes() {
        // Used by the serializer
    }
}