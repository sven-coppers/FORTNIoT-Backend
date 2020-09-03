package sven.phd.iot.scenarios;

import java.util.ArrayList;
import java.util.List;

public class Preset {
    private List<String> newDeviceSets;
    private List<String> newRuleSets;
    private List<String> newStateSets;
    private List<String> conflictVerifiers;

    public Preset() {
        this.newDeviceSets = new ArrayList();
        this.newStateSets = new ArrayList();
        this.newRuleSets = new ArrayList();
        this.conflictVerifiers = new ArrayList<>();
    }

    public Preset(List<String> newDeviceSets, List<String> newStateSets, List<String> newRuleSets, List<String> conflictVerifiers) {
        this.newDeviceSets = newDeviceSets;
        this.newRuleSets = newRuleSets;
        this.newStateSets = newStateSets;
        this.conflictVerifiers = conflictVerifiers;
    }

    public void addDeviceSet(String newItem) {
        this.newDeviceSets.add(newItem);
    }

    public void addRuleSet(String newItem) {
        this.newRuleSets.add(newItem);
    }

    public void addStateSet(String newItem) {
        this.newStateSets.add(newItem);
    }

    public void addConflictVerifier(String newItem) {
        this.conflictVerifiers.add(newItem);
    }

    public List<String> getNewDeviceSets() {
        return newDeviceSets;
    }

    public List<String> getNewRuleSets() {
        return newRuleSets;
    }

    public List<String> getNewStateSets() {
        return newStateSets;
    }

    public List<String> getConflictVerifiers() {
        return conflictVerifiers;
    }
}
