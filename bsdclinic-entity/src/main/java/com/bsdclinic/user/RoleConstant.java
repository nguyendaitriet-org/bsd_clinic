package com.bsdclinic.user;

import java.util.Arrays;
import java.util.List;

public enum RoleConstant {
    ADMIN,
    DOCTOR,
    STAFF;

    public static List<String> getAllNames() {
        return Arrays.stream(RoleConstant.values()).map(RoleConstant::name).toList();
    }
}
