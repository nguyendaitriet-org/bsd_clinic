package com.bsdclinic.category;

import java.util.Arrays;
import java.util.List;

public enum CategoryType {
    POST,
    VIDEO,
    MEDICINE,
    MEDICAL_SERVICE;

    public static List<String> getAllNames() {
        return Arrays.stream(CategoryType.values()).map(CategoryType::name).toList();
    }

}
