package com.bsdclinic.clinic_info;

import java.util.Arrays;
import java.util.List;

public enum DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    public static List<String> getAllNames() {
        return Arrays.stream(DayOfWeek.values()).map(DayOfWeek::name).toList();
    }
}
