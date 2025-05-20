package com.bsdclinic.user;

import java.util.Arrays;
import java.util.List;

public enum Gender {
    MALE,
    FEMALE;

    public static List<String> getAllNames() {
        return Arrays.stream(Gender.values()).map(Gender::name).toList();
    }
}
