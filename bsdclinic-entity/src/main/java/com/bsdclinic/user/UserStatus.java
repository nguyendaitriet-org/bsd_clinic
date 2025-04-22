package com.bsdclinic.user;

import java.util.Arrays;
import java.util.List;

public enum UserStatus {
    ACTIVE,
    BLOCKED;

    public static List<String> getAllNames() {
        return Arrays.stream(RoleConstant.values()).map(RoleConstant::name).toList();
    }
}
