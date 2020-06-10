package sven.phd.iot.predictions;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.hassio.updates.HassioRuleExecutionEvent;

import java.util.List;


public class CausalNode {
    private HassioState hassioState;
    private HassioRuleExecutionEvent executionEvent;

    public CausalNode(HassioState hassioState, HassioRuleExecutionEvent executionEvent) {
        this.hassioState = hassioState;
        this.executionEvent = executionEvent;
    }

    public HassioState getHassioState() {
        return hassioState;
    }

    public void setHassioState(HassioState hassioState) {
        this.hassioState = hassioState;
    }

    public void print(int depth) {
        printTabs(depth);

        if(this.hassioState == null) {
            System.out.println("root");
        } else {
            System.out.println(this.hassioState.entity_id + " - " + this.hassioState.state);
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

    public HassioState getState() {
        return this.hassioState;
    }

    public HassioRuleExecutionEvent getExecutionEvent() {
        return executionEvent;
    }
}
