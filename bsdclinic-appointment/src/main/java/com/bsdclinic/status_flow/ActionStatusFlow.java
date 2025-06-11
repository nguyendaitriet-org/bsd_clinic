package com.bsdclinic.status_flow;

import com.bsdclinic.appointment.ActionStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ActionStatusFlow {

    private final Map<ActionStatus, Set<ActionStatus>> transitions = new HashMap<>();

    public ActionStatusFlow() {
        transitions.put(ActionStatus.PENDING, Set.of(ActionStatus.ACCEPTED, ActionStatus.REJECTED));
        transitions.put(ActionStatus.ACCEPTED, Set.of(ActionStatus.CHECKED_IN, ActionStatus.REJECTED));
        transitions.put(ActionStatus.CHECKED_IN, Set.of(ActionStatus.EXAMINING));
        transitions.put(ActionStatus.EXAMINING, Set.of(ActionStatus.ADVANCED, ActionStatus.FINISHED));
        transitions.put(ActionStatus.ADVANCED, Set.of(ActionStatus.FINISHED));
        transitions.put(ActionStatus.FINISHED, Set.of(ActionStatus.UNPAID, ActionStatus.PAID));
        transitions.put(ActionStatus.UNPAID, Set.of(ActionStatus.PAID));
        transitions.put(ActionStatus.PAID, Set.of(ActionStatus.FINISHED)); // allow cycle if needed
        transitions.put(ActionStatus.REJECTED, Set.of()); // no transitions
    }

    public boolean canTransition(ActionStatus from, ActionStatus to) {
        return transitions.getOrDefault(from, Set.of()).contains(to);
    }

    public Set<ActionStatus> getNextStates(ActionStatus current) {
        return transitions.getOrDefault(current, Set.of());
    }
}