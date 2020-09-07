package sven.phd.iot.predictions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sven.phd.iot.hassio.states.HassioDateDeserializer;
import sven.phd.iot.hassio.states.HassioDateSerializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CausalLayer {
    @JsonProperty("causal_nodes") private List<CausalNode> causalNodes;

    @JsonDeserialize(using = HassioDateDeserializer.class)
    @JsonSerialize(using = HassioDateSerializer.class)
    @JsonProperty("causal_layer_date") public Date layerDate;

    public CausalLayer(Date layerDate) {
        this.causalNodes = new ArrayList<>();
        this.layerDate = layerDate;
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
        System.out.print(layerDate + ": ");

        for(CausalNode deviceState : causalNodes) {
            System.out.print(deviceState.getState().entity_id + " = " + deviceState.getState().state + ", ");
        }

        System.out.println();
    }
}