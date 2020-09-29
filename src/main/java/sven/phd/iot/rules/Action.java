package sven.phd.iot.rules;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.hassio.states.HassioState;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/* @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "action_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LightOnAction.class, name = "LightOnAction"),
        @JsonSubTypes.Type(value = LightOffAction.class, name = "LightOffAction"),
        @JsonSubTypes.Type(value = OutletAction.class, name = "OutletAction"),
        @JsonSubTypes.Type(value = StartCleaningAction.class, name = "StartCleaningAction"),
        @JsonSubTypes.Type(value = StateAction.class, name = "StateAction"),
        @JsonSubTypes.Type(value = ThermostatStateAction.class, name = "ThermostatStateAction")
}) */
abstract public class Action {
    private static int random = 0;

    @JsonProperty("id") public String id;
    @JsonProperty("description") public String description;
    @JsonProperty("enabled") public Boolean enabled;
    @JsonProperty("action_name") public String actionName; // TODO this is unnecessary when using the jsonSubTypes


    // For deserialization
    public Action() {
    }

    public Action(String description) {
        this.id = ("actionId" + random++);
        this.description = description;
        this.enabled = true;
        this.actionName = this.getClass().getName();
    }

  //  abstract public void previewHandler(Map<String, HassioState> newState);
   // abstract public void eventHandler(Map<String, HassioState> newState);

    abstract public List<HassioState> simulate(Date datetime, HashMap<String, HassioState> hassioStates);

    /*MATHIAS*/
    public String getId() { return id; }

    /*MATHIAS*/
    public String getDeviceID() { return ""; };

    /*MATHIAS*/
    public boolean isSimilar(Action other) { return other.description.equals(description); }

    public boolean isEnabled() {
        return this.enabled;
    }


    @Override
    public String toString() {
        return this.description;
    }

    public void generateNewID() {this.id = ("actionId" + random++);};
}
