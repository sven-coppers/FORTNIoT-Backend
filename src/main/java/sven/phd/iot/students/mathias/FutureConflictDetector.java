package sven.phd.iot.students.mathias;

import sven.phd.iot.ContextManager;
import sven.phd.iot.hassio.states.HassioConflictState;
import sven.phd.iot.hassio.states.HassioConflictingAttribute;
import sven.phd.iot.hassio.states.HassioState;

import java.util.ArrayList;
import java.util.List;

public class FutureConflictDetector {

    public FutureConflictDetector() {
    }

    public List<HassioConflictState> getFutureConflicts() {
        List<HassioState> futureStates = ContextManager.getInstance().getStateFuture();
        List<HassioConflictState> result = new ArrayList<>();

        /**
         * Find race conditions
         */
        for (HassioState comparingState: futureStates) {
            for (HassioState state: futureStates) {
                if (state.datetime.compareTo(comparingState.datetime) == 0
                && state.entity_id == comparingState.entity_id) {
                    HassioConflictState conflictState = state.compareAttributes(comparingState);
                    if (conflictState != null) {
                        result.add(conflictState);


                        System.out.println("Time: " + conflictState.datetime.toString());
                        System.out.println("Entity: " + conflictState.entity_id);

                        for (HassioConflictingAttribute attribute: conflictState.conflicts) {
                            System.out.println(attribute.attribute_name + ": " + attribute.value1 + "    &   " + attribute.value2);
                        }
                        System.out.println("--------------------------------------------------------------");

                    }
                }
            }
        }

        return result;
    }
}
