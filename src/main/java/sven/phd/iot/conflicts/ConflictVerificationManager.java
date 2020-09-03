package sven.phd.iot.conflicts;

import sven.phd.iot.scenarios.cases.LivingTempDevices;

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