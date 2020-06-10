package sven.phd.iot.predictions;

import sven.phd.iot.hassio.states.HassioState;

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
}
