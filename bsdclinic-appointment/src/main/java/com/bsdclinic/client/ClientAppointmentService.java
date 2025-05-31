package com.bsdclinic.client;

import com.bsdclinic.client.response.AvailableAppointmentSlot;
import com.bsdclinic.dto.AppointmentDto;

import java.time.LocalDate;

public interface ClientAppointmentService {
     AvailableAppointmentSlot getAvailableSlots(LocalDate registerDate);
     void createNewAppointment(AppointmentDto appointmentDto);
}
