package sven.phd.iot.predictions;

import com.fasterxml.jackson.annotation.JsonProperty;
import sven.phd.iot.rules.Action;

import java.util.ArrayList;
import java.util.List;

public class ConflictSolution {
    // When to apply this solution?
    @JsonProperty("entity_id") public String entity_id;
  //  @JsonProperty("a") public String entity_id;

    // How to solve it
    @JsonProperty("before_actions") private List<Action> beforeActions; // e.g. disable an action from a rule
    @JsonProperty("actions") private List<Action> actions;       // e.g. Perform a manual action
    @JsonProperty("after_actions") private List<Action> afterActions;  // e.g. enable the action from the rule again

    public ConflictSolution() {
        this.beforeActions = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.afterActions = new ArrayList<>();
    }
}
