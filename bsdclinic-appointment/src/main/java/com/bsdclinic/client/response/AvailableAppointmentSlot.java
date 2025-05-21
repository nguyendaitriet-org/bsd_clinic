package com.bsdclinic.client.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class AvailableAppointmentSlot {
    private LocalDate registerDate;
    private String day;
    private List<String> availableSlots;
}
