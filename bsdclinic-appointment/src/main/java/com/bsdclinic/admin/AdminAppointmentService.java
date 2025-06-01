package com.bsdclinic.admin;

import com.bsdclinic.client.response.AvailableAppointmentSlot;
import com.bsdclinic.dto.AppointmentDto;

import java.time.LocalDate;

public interface AdminAppointmentService {
     AvailableAppointmentSlot getAvailableSlots(LocalDate registerDate);
     void createNewAppointment(AppointmentDto appointmentDto);
}
