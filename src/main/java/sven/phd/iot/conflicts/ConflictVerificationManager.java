package sven.phd.iot.conflicts;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.Future;
import sven.phd.iot.scenarios.cases.BedroomTempDevices;

import java.util.*;

public class ConflictVerificationManager {
    private HashMap<String, ConflictVerifier> conflictVerifiers;

    public ConflictVerificationManager() {
        this.conflictVerifiers = new HashMap<>();

        this.conflictVerifiers.put("redundancies", new RedundancyVerifier());
        this.conflictVerifiers.put("loops", new LoopVerifier());
        this.conflictVerifiers.put("inconsistencies", new InconsistencyVerifier());
        this.conflictVerifiers.put("no_effect", new NoEffectVerifier());
        this.conflictVerifiers.put("udc_bedroom_temp", new TemperatureConflictVerifier(BedroomTempDevices.BEDROOM_TEMPERATURE, Arrays.asList(BedroomTempDevices.BEDROOM_HEATING), Arrays.asList(BedroomTempDevices.BEDROOM_AIRCO)));
    }

    /**
     * Check if the hassioChange causes this trigger to be triggered
     *
     * @param future
     * @param newState
     * @return a list Conflicts that may be caused
     */
    public List<Conflict> verifyConflicts(Date simulationTime, Future future, HassioState newState) {
        List<Conflict> conflicts = new ArrayList<>();

        for(String verifierID : this.conflictVerifiers.keySet()) {
            ConflictVerifier verifier = this.conflictVerifiers.get(verifierID);

            if(verifier.isInterestedIn(newState)) {
                List<Conflict> newConflicts = this.conflictVerifiers.get(verifierID).verifyConflicts(simulationTime, future, newState);

                for(Conflict newConflict : newConflicts) {
                    newConflict.print();
                    conflicts.add(newConflict);
                }
            }
        }

        return conflicts;
    }


    public void setAllVerifiersEnabled(boolean enabled) {
        for(String verifierID : this.conflictVerifiers.keySet()) {
            this.conflictVerifiers.get(verifierID).setEnabled(enabled);
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
        this.setAllVerifiersEnabled(false);

        for(String key : newActiveVerifiers) {
            if(this.conflictVerifiers.containsKey(key)) {
                this.conflictVerifiers.get(key).setEnabled(true);
            }
        }

    }

    public void setVerifierEnabled(String verifierID, Boolean enabled) {
        this.conflictVerifiers.get(verifierID).setEnabled(enabled);
    }
}