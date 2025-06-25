package com.bsdclinic.appointment;

import java.util.Arrays;
import java.util.List;

public enum ActionStatus {
    PENDING,
    REJECTED,
    ACCEPTED,
    CHECKED_IN,
    EXAMINING,
    ADVANCED,
    FINISHED,
    FINISHED_NO_PAY,
    PAID,
    UNPAID;

    public static List<String> getAllNames() {
        return Arrays.stream(ActionStatus.values()).map(ActionStatus::name).toList();
    }
}
