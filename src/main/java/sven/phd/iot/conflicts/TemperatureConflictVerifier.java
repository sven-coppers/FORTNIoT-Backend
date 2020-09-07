package sven.phd.iot.conflicts;

import sven.phd.iot.hassio.change.HassioChange;
import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.CausalStack;
import sven.phd.iot.students.mathias.states.Conflict;

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
    public boolean isTriggeredBy(HassioChange hassioChange) {
        return true;
    }

    @Override
    public List<Conflict> verifyConflicts(Date simulationTime, HashMap<String, HassioState> hassioStates, CausalStack causalStack) {
        List<Conflict> conflicts = new ArrayList<>();
        List<String> conflictingEntities = new ArrayList<>();

        int numHeatersOn = 0;
        int numCoolersOn = 0;

        // Count the number of heaters  turned on
        for(String heaterID : this.heaterIDs) {
            HassioState heaterState = null;

            if(hassioStates.get(heaterID) != null) {
                heaterState = hassioStates.get(heaterID);
            }

            if(causalStack.getChange(heaterID) != null) {
                heaterState = causalStack.getChange(heaterID).getState();
            }

            if(heaterState != null && heaterState.state.equals("heating")) {
                numHeatersOn++;
                conflictingEntities.add(heaterState.entity_id);
            }
        }

        // Count the number of heaters  turned on
        for(String coolerID : this.coolerIDs) {
            HassioState coolerState = null;

            if (hassioStates.get(coolerID) != null) {
                coolerState = hassioStates.get(coolerID);
            }

            if (causalStack.getChange(coolerID) != null) {
                coolerState = causalStack.getChange(coolerID).getState();
            }

            if (coolerState != null && coolerState.state.equals("cooling")) {
                numCoolersOn++;
                conflictingEntities.add(coolerState.entity_id);
            }
        }

        // If there is at least one heater and at least one cooler turned on at the same time, throw conflict
        if(numCoolersOn > 0 && numHeatersOn > 0) {
            conflicts.add(new Conflict("user_defined_conflict_bedroom_temperature", simulationTime, conflictingEntities, new ArrayList<>()));
            // The cause of a conflicting state can be unknown
        }

        return conflicts;
    }
}