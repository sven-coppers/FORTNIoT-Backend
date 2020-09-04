package sven.phd.iot.predictions;

import java.util.*;

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
                if(layer.getCausalNode(i).getState().entity_id.equals(entity_id)) {
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
            result.addAll(layer.getCausalNodes());
        }

        return result;
    }

    /**
     * Retrieves the node that is in the highest position in the stack
     * @param nodes
     * @return null when no node is found to be solely the highest node
     */
    public CausalNode getHighestNode(List<CausalNode> nodes) {
        CausalNode startingNode = null;
        for (int i = 0; i < stack.size() && startingNode == null; ++i) {
            CausalLayer layer = stack.get(i);
            for (CausalNode node : layer.getCausalNodes()) {
                if (nodes.contains(node)) {
                    if (startingNode == null) {
                        startingNode = node;
                    } else {
                        // multiple nodes are found in the same layer, so no node is solely the highest
                        return null;
                    }

                }
            }
        }
        return startingNode;
    }
}
