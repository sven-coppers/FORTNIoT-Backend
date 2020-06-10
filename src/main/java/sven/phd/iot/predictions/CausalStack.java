package sven.phd.iot.predictions;

import sven.phd.iot.hassio.states.HassioState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class CausalStack {
    private List<CausalLayer> stack;

    public CausalStack() {
        this.stack = new Stack<>();
    }

    public void addLayer(CausalLayer layer) {
        stack.add(layer);
    }

    public void print() {
        Iterator<CausalLayer> stackIterator = stack.iterator();
        int i = 0;

        while(stackIterator.hasNext()) {
            System.out.print("layer " + i + ": ");
            stackIterator.next().print();

            System.out.println();
            ++i;
        }

        System.out.println("------");
    }

    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    public CausalLayer getLayer(int i) {
        return this.stack.get(i);
    }

    public int getNumLayers() {
        return this.stack.size();
    }

    public CausalLayer getTopLayer() {
        return this.stack.get(this.stack.size() - 1);
    }

    public boolean hasChange(String entity_id) {
        Iterator<CausalLayer> stackIterator = stack.iterator();

        while(stackIterator.hasNext()) {
            CausalLayer layer = stackIterator.next();

            for(int i = 0; i < layer.getNumStates(); ++i) {
                if(layer.getState(i).getState().entity_id.equals(entity_id)) {
                    return true;
                }
            }
        }

        return false;
    }

    public List<CausalNode> flatten() {
        Iterator<CausalLayer> stackIterator = stack.iterator();
        List<CausalNode> result = new ArrayList<>();

        while(stackIterator.hasNext()) {
            CausalLayer layer = stackIterator.next();
            result.addAll(layer.getStates());
        }

        return result;
    }
}
