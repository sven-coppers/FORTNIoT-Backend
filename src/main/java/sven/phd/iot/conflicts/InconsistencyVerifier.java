package sven.phd.iot.conflicts;

import sven.phd.iot.hassio.states.HassioState;
import sven.phd.iot.predictions.Future;
import sven.phd.iot.students.mathias.states.Conflict;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InconsistencyVerifier extends ConflictVerifier {
    @Override
    public List<Conflict> verifyConflicts(Date simulationTime, Future future, HassioState hassioState) {
        List<Conflict> conflicts = new ArrayList<>();

        // Do the check for every node in the new layer
      /*  for(CausalNode newNode : newCausalLayer.getCausalNodes()) {
            if(this.conflictVerifiers.get(verifierID).isTriggeredBy())
        } */


        // Group all potential changes by entityID
     /*   List<CausalNode> potentialChanges = causalStack.flatten();
        List<String> alreadyVisited = new ArrayList<>();

        for(int i = 0; i < potentialChanges.size(); ++i) {
            List<CausalNode> conflictingChanges = new ArrayList<>();
            String initialEntityID = potentialChanges.get(i).getState().entity_id;
            boolean newConflictFound = false;

            // Don't visit an entity multiple times
            if (!alreadyVisited.contains(initialEntityID)) {
                alreadyVisited.add(initialEntityID);

                for (int j = i; j < potentialChanges.size(); ++j) {
                    if (potentialChanges.get(j).getState().entity_id.equals(initialEntityID)) {
                        conflictingChanges.add(potentialChanges.get(j));
                    }
                }

                if (conflictingChanges.size() > 1) {
                    System.out.println("CONFLICT DETECTED FOR " + initialEntityID);
                }
            }
        } */

        return conflicts;
    }
}
