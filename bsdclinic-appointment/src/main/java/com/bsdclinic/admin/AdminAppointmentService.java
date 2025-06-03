package com.bsdclinic.admin;

import com.bsdclinic.dto.AppointmentDto;
import com.bsdclinic.dto.request.AppointmentFilter;
import com.bsdclinic.response.DatatableResponse;

public interface AdminAppointmentService {
     void createNewAppointment(AppointmentDto appointmentDto);
     DatatableResponse getAppointmentsByFilter(AppointmentFilter appointmentFilter);
}
