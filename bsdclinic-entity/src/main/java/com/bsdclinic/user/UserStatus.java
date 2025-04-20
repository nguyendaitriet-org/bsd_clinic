package com.bsdclinic.user;

import java.util.Arrays;
import java.util.List;

public enum UserStatus {
    ACTIVE("active"),
    BLOCKED("blocked");

    private final String value;

    UserStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserStatus parse(String value) {
        UserStatus[] values = values();
        for (UserStatus userStatus : values) {
            if (userStatus.name().equals(value)) {
                return userStatus;
            }
        }
        throw new IllegalArgumentException("Cannot parse " + value);
    }

    public static List<String> getAllNames() {
        return Arrays.stream(RoleConstant.values()).map(RoleConstant::name).toList();
    }
}
