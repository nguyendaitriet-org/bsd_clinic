package com.bsdclinic.dto.response;

import java.sql.Timestamp;

public interface IMedicalRecordResponse {
    String getAppointmentId();
    String getMedicalRecordId();
    String getPatientName();
    String getPatientPhone();
    Timestamp getCreatedAt();
    String getDoctorName();
}
