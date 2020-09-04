package sven.phd.iot.predictions;

import java.util.ArrayList;
import java.util.List;

public class CausalLayer {
    private List<CausalNode> causalNodes;

    public CausalLayer() {
        this.causalNodes = new ArrayList<>();
    }

    public void addCausalNode(CausalNode state) {
        this.causalNodes.add(state);
    }

    public List<CausalNode> getCausalNodes() {
        return causalNodes;
    }

    public boolean isEmpty() {
        return this.causalNodes.isEmpty();
    }

    public int getNumStates() {
        return this.causalNodes.size();
    }

    public CausalNode getCausalNode(int index) {
        return this.causalNodes.get(index);
    }

    public void addAll(List<CausalNode> newStates) {
        this.causalNodes.addAll(newStates);
    }

    public void print() {
        for(CausalNode deviceState : causalNodes) {
            System.out.print(deviceState.getState().entity_id + " = " + deviceState.getState().state + ", ");
        }
    }
}