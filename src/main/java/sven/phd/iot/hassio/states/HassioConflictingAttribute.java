package sven.phd.iot.hassio.states;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class HassioConflictingAttribute implements Serializable {
    @JsonProperty("attribute_name") public String attribute_name;
    @JsonProperty("value1") public String value1;
    @JsonProperty("value2") public String value2;

    public HassioConflictingAttribute() {
        // default constructor
    }

    public HassioConflictingAttribute(String attribute_name, String value1, String value2) {
        this.attribute_name = attribute_name;
        this.value1 = value1;
        this.value2 = value2;
    }
}
