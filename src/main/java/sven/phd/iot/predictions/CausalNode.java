package sven.phd.iot.predictions;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// Intercept all actions in the frame
//HashMap<String, List<ProposedActionExecution>> proposedActionExecutions = new HashMap<>();

//zon -> proposedStates: [state: {...}, executionEvent: null},

// Inconsistency
        /* living_spots -> proposedStates: [
        // { state: {...}, executionEvent: {rule_id: "rule.nobody_home_lights", action_ID: "actionId7" (via action context id)}},
        // { state: {...}, executionEvent: {rule_id: "rule.sun_set_lights", action_ID: "actionId4" (via action context id)}}
        //] */

// Loop
        /* living_spots -> proposedStates: [
        // { state: {...}, executionEvent: {rule_id: "rule.sun_set_lights", action_ID: "actionId4" (via action context id)}}
        // { state: {...}, executionEvent: {rule_id: "rule.sun_set_lights", action_ID: "actionId4" (via action context id)}}
        //] */

public class CausalNode {
   // private String condition;
    private List<CausalNode> consequences;
    private HassioState hassioState;
    private CausalNode parent;
    private HassioRuleExecutionEvent executionEvent;
    private LinkedList<CausalNode> queue;

    public CausalNode(HassioState hassioState, HassioRuleExecutionEvent executionEvent) {
        this.hassioState = hassioState;
        this.executionEvent = executionEvent;
        this.consequences = new ArrayList<>();
        this.parent = null;
        this.queue = new LinkedList<>();
    }

    public void pushToQueue(CausalNode node) {
        this.queue.add(node);
    }

    public void pushToQueue(List<CausalNode> nodes) {
        this.queue.addAll(nodes);
    }

    public CausalNode popFromQueue() {
        if(this.queue.isEmpty()) {
            return null;
        } else {
            return this.queue.pop();
        }
    }

    public HassioState getHassioState() {
        return hassioState;
    }

    public void setHassioState(HassioState hassioState) {
        this.hassioState = hassioState;
    }

    public CausalNode getParent() {
        return parent;
    }

    public void setParent(CausalNode parent) {
        this.parent = parent;
    }


    public void addChild(CausalNode node) {
        consequences.add(node);
        node.setParent(this);
    }

    public void print(int depth) {
        printTabs(depth);

        if(this.hassioState == null) {
            System.out.println("root");
        } else {
            System.out.println(this.hassioState.entity_id + " - " + this.hassioState.state);
        }

        for(CausalNode node : consequences) {
            node.print(depth + 1);
        }
    }

    public void print() {
        this.print(0);
    }

    private void printTabs(int depth) {
        for(int i = 0; i < depth; ++i) {
            System.out.print("\t");
        }
    }

    public boolean isEmpty() {
        return this.consequences.isEmpty();
    }

    public List<CausalNode> getChildren() {
        return this.consequences;
    }

    public HassioState getState() {
        return this.hassioState;
    }

    public CausalNode peekFromQueue() {
        return this.queue.peek();
    }

    public List<CausalNode> getQueue() {
        return this.queue;
    }

    public boolean hadSomethingQueuedThatDependsOn(String deviceID) {
        for(CausalNode node : this.queue) {
            if(node.executionEvent.triggerContexts.containsKey(deviceID)) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param deviceID
     * @return null if it is safe
     */
    public CausalNode getOldestAncestorThatHasSomethingQueuedThatDependsOn(String deviceID) {
        // Try the oldest stuff first
        if(this.getParent() != null) {
            CausalNode potentialResult = this.getParent().getOldestAncestorThatHasSomethingQueuedThatDependsOn(deviceID);

            if(potentialResult != null) {
                return potentialResult;
            }
        }

        // Try ourselves
        if(this.hadSomethingQueuedThatDependsOn(deviceID)) {
            return this;
        }

        return null;
    }

    public void clearChildren() {
        this.consequences.clear();
    }

    /*     public CausalNode getClosestAncestorAbout(String deviceID) {
        if(this.hassioState.entity_id.equals(deviceID)) return this;

        if(this.getParent() != null) {
            return this.getParent().getOldestAncestorThatDependedOn(deviceID);
        } else {
            return null;
        }
    } */
}
