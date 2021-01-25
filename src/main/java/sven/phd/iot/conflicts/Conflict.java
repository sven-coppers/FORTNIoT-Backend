package sven.phd.iot.conflicts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;
import sven.phd.iot.hassio.states.HassioState;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Conflict {
    @JsonProperty("conflicting_states") public List<HassioState> conflictingStates;

    @JsonProperty("conflict_type") public String conflictType;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("conflict_time") protected Date conflictTime;

    @JsonProperty("detrimental") protected boolean detrimental;
    @JsonIgnore
    private ConflictVerifier conflictVerifier;

    public Conflict(ConflictVerifier conflictVerifier, Date date) {
        this(conflictVerifier, date, new ArrayList<>());
    }

    public Conflict(ConflictVerifier conflictVerifier, Date date, List<HassioState> conflictingStates) {
        this.conflictVerifier = conflictVerifier;
        this.conflictingStates = conflictingStates;
        this.detrimental = false;
        this.conflictType = conflictVerifier.getConflictType();
        this.conflictTime = date;
    }

    public void addConflictState(HassioState hassioState) {
        if(!this.containsState(hassioState)) this.conflictingStates.add(hassioState);

    }

    public List<HassioState> getConflictingStates() {
        return conflictingStates;
    }

    public void addConflictingStates(List<HassioState> conflictingStates) {
        for(HassioState newConflictingState : conflictingStates) {
            this.addConflictState(newConflictingState);
        }
    }

    public boolean hasOverlappingStates(Conflict otherConflict) {
        for(HassioState otherConflictState : otherConflict.conflictingStates) {
            if (this.containsState(otherConflictState)) return true;
        }

        return false;
    }

    public boolean containsState(HassioState otherConflictState) {
        for(HassioState existingConflictState : this.conflictingStates) {
            if(existingConflictState.context.id.equals(otherConflictState.context.id)) {
                return true;
            }
        }

        return false;
    }

    public void setDetrimental(boolean detrimental) {
        this.detrimental = detrimental;
    }

    public boolean isDetrimental() {
        return this.detrimental;
    }

    public void print() {
        System.out.print("\t" + this.conflictType + ": ");

        for(HassioState state : this.conflictingStates) {
            System.out.print(state.entity_id + " = " + state.state + ", ");
        }

        System.out.println();
    }

    public ConflictVerifier getConflictVerifier() {
        return conflictVerifier;
    }
}
