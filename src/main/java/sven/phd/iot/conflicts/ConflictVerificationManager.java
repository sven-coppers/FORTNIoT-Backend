package sven.phd.iot.conflicts;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.CausalStack;
import sven.phd.iot.scenarios.cases.LivingTempDevices;
import sven.phd.iot.students.mathias.states.Conflict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConflictVerificationManager {
    private HashMap<String, ConflictVerifier> conflictVerifiers;

    public ConflictVerificationManager() {
        this.conflictVerifiers = new HashMap<>();

        this.conflictVerifiers.put("redundancies", new RedundancyVerifier());
        this.conflictVerifiers.put("loops", new LoopVerifier());
        this.conflictVerifiers.put("inconsistencies", new InconsistencyVerifier());
        this.conflictVerifiers.put("udc_bedroom_temperature", new TemperatureConflictVerifier(LivingTempDevices.LIVING_TEMPERATURE));
    }

    /**
     * Check if the hassioChange causes this trigger to be triggered
     * @param previousStates a map with states for each device
     * @param causalStack the causalStack of the current tick
     * @return a list Conflicts that may be caused
     */
    public List<Conflict> verifyConflicts(HashMap<String, HassioState> previousStates, CausalStack causalStack) {
        List<Conflict> conflicts = new ArrayList<>();

        for(String verifierID : this.conflictVerifiers.keySet()) {
            if(this.conflictVerifiers.get(verifierID).isEnabled()) { // Maybe also ask if it is interested?
                conflicts.addAll(this.conflictVerifiers.get(verifierID).verifyConflicts(previousStates, causalStack));
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