package com.bsdclinic.admin;

import com.bsdclinic.dto.AppointmentDto;
import com.bsdclinic.dto.request.AppointmentFilter;
import com.bsdclinic.dto.request.AppointmentUpdate;
import com.bsdclinic.response.DatatableResponse;

import java.util.Map;

public interface AdminAppointmentService {
     void createNewAppointment(AppointmentDto appointmentDto);
     DatatableResponse getAppointmentsByFilter(AppointmentFilter appointmentFilter);
     Map<String, Integer> getAppointmentStatusCount();
     void updateAppointment(String appointmentId, AppointmentUpdate appointmentUpdate);
}
