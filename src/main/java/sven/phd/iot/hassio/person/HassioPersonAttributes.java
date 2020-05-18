package sven.phd.iot.hassio.person;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import sven.phd.iot.hassio.states.HassioAttributes;

public class HassioPersonAttributes extends HassioAttributes {
    @JsonProperty("editable") boolean editable;
    @JsonProperty("gps_accuracy") int gpsAccuracy;
    @JsonProperty("id") String id;
    @JsonProperty("latitude") float latitude;
    @JsonProperty("longitude") float longitude;
    @JsonProperty("source") String source;
    @JsonProperty("user_id") String userID;

    public HassioPersonAttributes() {
        // Used by the serializer
    }
    @JsonGetter("id")
    public String getID() {
        return this.id;
    }
    @JsonSetter("id")
    public void setID(String id) {
        this.id = id;
    }
    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public int getGpsAccuracy() {
        return gpsAccuracy;
    }

    public void setGpsAccuracy(int gpsAccuracy) {
        this.gpsAccuracy = gpsAccuracy;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}