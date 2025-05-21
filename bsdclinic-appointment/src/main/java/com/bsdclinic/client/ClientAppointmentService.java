package com.bsdclinic.client;

import com.bsdclinic.client.response.AvailableAppointmentSlot;

import java.time.LocalDate;

public interface ClientAppointmentService {
     AvailableAppointmentSlot getAvailableSlots(LocalDate registerDate);
}
