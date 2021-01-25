package sven.phd.iot.conflicts;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.Future;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

// A heater and a cooler in the same room may never be turned on at the same
public class TemperatureConflictVerifier extends ConflictVerifier {
    private String tempSensorID;
    private List<String> coolerIDs;
    private List<String> heaterIDs;

    public TemperatureConflictVerifier(String tempSensorID, List<String> heaterIDs, List<String> coolerIDs) {
        this.tempSensorID = tempSensorID;
        this.heaterIDs = heaterIDs;
        this.coolerIDs = coolerIDs;
    }

    @Override
    public boolean isInterestedIn(HassioState hassioState) {
        String changedEntity = hassioState.entity_id;

        return this.heaterIDs.contains(changedEntity) || this.coolerIDs.contains(changedEntity);
    }

    @Override
    public List<Conflict> verifyConflicts(Date simulationTime, Future future, HassioState newState) {
        List<Conflict> conflicts = new ArrayList<>();
        HashMap<String, HassioState> lastStates = future.getLastStates();

        List<HassioState> activeHeaterStates = new ArrayList<>();
        List<HassioState> activeCoolerStates = new ArrayList<>();

        if(newState.state.equals("heating")) {
            activeHeaterStates.add(newState);
        }

        if(newState.state.equals("cooling")) {
            activeCoolerStates.add(newState);
        }

        // Count the number of heaters  turned on
        for(String heaterID : this.heaterIDs) {
            HassioState heaterState = lastStates.get(heaterID);

            if(heaterState != null && heaterState.state.equals("heating")) {
                activeHeaterStates.add(heaterState);
            }
        }

        // Count the number of heaters  turned on
        for(String coolerID : this.coolerIDs) {
            HassioState coolerState = lastStates.get(coolerID);

            if (coolerState != null && coolerState.state.equals("cooling")) {
                activeCoolerStates.add(coolerState);
            }
        }

        // If there is at least one heater and at least one cooler turned on at the same time, throw conflict
        if(activeHeaterStates.size() > 0 && activeCoolerStates.size() > 0) {
            Conflict conflict = new Conflict(this, simulationTime);
            conflict.addConflictingStates(activeHeaterStates);
            conflict.addConflictingStates(activeCoolerStates);
            conflicts.add(conflict);
        }

        return conflicts;
    }

    @Override
    public String getConflictType() {
        return "user_defined_conflict_bedroom_temperature";
    }
}