package sven.phd.iot.students.mathias.states;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;

import java.util.Date;

public class HassioConflictingActionState {
    @JsonProperty("action_id") public String action_id;
    @JsonProperty("rule_id") public String rule_id;

  //  @JsonDeserialize(using = HassioDateDeserializer.class)
  //  @JsonSerialize(using = HassioDateSerializer.class)
 //   @JsonProperty("triggered_time") public Date triggered_time;

    public HassioConflictingActionState(String action_id, String rule_id) {
        this.action_id = action_id;
        this.rule_id = rule_id;
       // this.triggered_time = triggered_time;
    }
}
