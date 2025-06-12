package com.bsdclinic.status_flow;

import com.bsdclinic.appointment.ActionStatus;
import com.bsdclinic.user.RoleConstant;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ActionStatusFlow {
    private final Map<ActionStatus, Set<ActionStatus>> transitions = new HashMap<>();
    private final Map<String, Set<String>> rolePermissions = new HashMap<>();

    public ActionStatusFlow() {
        transitions.put(ActionStatus.PENDING, Set.of(ActionStatus.ACCEPTED, ActionStatus.REJECTED));
        transitions.put(ActionStatus.ACCEPTED, Set.of(ActionStatus.CHECKED_IN, ActionStatus.REJECTED));
        transitions.put(ActionStatus.CHECKED_IN, Set.of(ActionStatus.EXAMINING, ActionStatus.REJECTED));
        transitions.put(ActionStatus.EXAMINING, Set.of(ActionStatus.ADVANCED, ActionStatus.CHECKED_IN));
        transitions.put(ActionStatus.ADVANCED, Set.of(ActionStatus.FINISHED));
        transitions.put(ActionStatus.FINISHED, Set.of(ActionStatus.UNPAID, ActionStatus.PAID));
        transitions.put(ActionStatus.UNPAID, Set.of(ActionStatus.PAID, ActionStatus.FINISHED));
        transitions.put(ActionStatus.PAID, Set.of(ActionStatus.FINISHED));
        transitions.put(ActionStatus.REJECTED, Set.of());

        // Role permissions for each transition
        addRolePermission(ActionStatus.CHECKED_IN, ActionStatus.EXAMINING, Set.of(RoleConstant.DOCTOR.name(), RoleConstant.ADMIN.name()));
        addRolePermission(ActionStatus.EXAMINING, ActionStatus.CHECKED_IN, Set.of(RoleConstant.DOCTOR.name(), RoleConstant.ADMIN.name()));
        addRolePermission(ActionStatus.ADVANCED, ActionStatus.FINISHED, Set.of(RoleConstant.DOCTOR.name(), RoleConstant.ADMIN.name()));
    }

    private void addRolePermission(ActionStatus from, ActionStatus to, Set<String> allowedRoles) {
        String key = key(from, to);
        rolePermissions.put(key, allowedRoles);
    }

    private String key(ActionStatus from, ActionStatus to) {
        return from.name() + "->" + to.name();
    }

    public boolean canTransition(ActionStatus from, ActionStatus to) {
        return transitions.getOrDefault(from, Set.of()).contains(to);
    }

    public Set<ActionStatus> getNextStatesByRole(ActionStatus from, String role) {
        return transitions.getOrDefault(from, Set.of()).stream()
                .filter(to -> isRoleAllowed(from, to, role))
                .collect(Collectors.toSet());
    }

    public boolean isRoleAllowed(ActionStatus from, ActionStatus to, String role) {
        String key = key(from, to);
        Set<String> allowed = rolePermissions.get(key);

        // If no role restriction is defined, allow for all roles
        return allowed == null || allowed.contains(role);
    }
}