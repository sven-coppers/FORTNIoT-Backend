package sven.phd.iot.predictions;

import java.util.ArrayList;
import java.util.List;

public class CausalLayer {
    private List<CausalNode> hassioStates;

    public CausalLayer() {
        this.hassioStates = new ArrayList<>();
    }

    public void addState(CausalNode state) {
        this.hassioStates.add(state);
    }

    public List<CausalNode> getStates() {
        return hassioStates;
    }

    public boolean isEmpty() {
        return this.hassioStates.isEmpty();
    }

    public int getNumStates() {
        return this.hassioStates.size();
    }

    public CausalNode getState(int index) {
        return this.hassioStates.get(index);
    }

    public void addAll(List<CausalNode> newStates) {
        this.hassioStates.addAll(newStates);
    }

    public void print() {
        for(CausalNode deviceState : hassioStates) {
            System.out.print(deviceState.getState().entity_id + " = " + deviceState.getState().state + ", ");
        }
    }
}