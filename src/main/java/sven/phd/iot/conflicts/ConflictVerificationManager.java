package sven.phd.iot.conflicts;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.CausalLayer;
import sven.phd.iot.predictions.CausalNode;
import sven.phd.iot.predictions.CausalStack;
import sven.phd.iot.predictions.Future;
import sven.phd.iot.scenarios.cases.BedroomTempDevices;
import sven.phd.iot.students.mathias.states.Conflict;

import java.util.*;

public class ConflictVerificationManager {
    private HashMap<String, ConflictVerifier> conflictVerifiers;

    public ConflictVerificationManager() {
        this.conflictVerifiers = new HashMap<>();

        this.conflictVerifiers.put("redundancies", new RedundancyVerifier());
        this.conflictVerifiers.put("loops", new LoopVerifier());
        this.conflictVerifiers.put("inconsistencies", new InconsistencyVerifier());
        this.conflictVerifiers.put("udc_bedroom_temperature", new TemperatureConflictVerifier(BedroomTempDevices.BEDROOM_TEMPERATURE, Arrays.asList(BedroomTempDevices.BEDROOM_HEATING), Arrays.asList(BedroomTempDevices.BEDROOM_AIRCO)));
    }

    /**
     * Check if the hassioChange causes this trigger to be triggered
     *
     * @param future
     * @return a list Conflicts that may be caused
     */
    public List<Conflict> verifyConflicts(Date simulationTime, Future future, List<CausalNode> newCausalNodes) {
        List<Conflict> conflicts = new ArrayList<>();

        for(String verifierID : this.conflictVerifiers.keySet()) {
            if(this.conflictVerifiers.get(verifierID).isEnabled()) { // Maybe also ask if it is interested?
                for(CausalNode newNode : newCausalNodes) {
                    if(this.conflictVerifiers.get(verifierID).isInterestedIn(newNode)) {
                        conflicts.addAll(this.conflictVerifiers.get(verifierID).verifyConflicts(simulationTime, future, newNode));
                    }
                }
            }
        }

        return conflicts;
    }


    public void disableAllVerifiers() {
        for(String verifierID : this.conflictVerifiers.keySet()) {
            this.conflictVerifiers.get(verifierID).setEnabled(false);
        }
    }

    public List<String> getAllVerifiers() {
        return new ArrayList<>(this.conflictVerifiers.keySet());
    }

    public List<String> getActiveVerifiers() {
        ArrayList<String> result = new ArrayList<>();

        for(String key : this.conflictVerifiers.keySet()) {
            if(this.conflictVerifiers.get(key).isEnabled()) {
                result.add(key);
            }
        }

        return result;
    }

    public void setActiveVerifiers(List<String> newActiveVerifiers) {
        this.disableAllVerifiers();

        for(String key : newActiveVerifiers) {
            if(this.conflictVerifiers.containsKey(key)) {
                this.conflictVerifiers.get(key).setEnabled(true);
            }
        }

    }
}