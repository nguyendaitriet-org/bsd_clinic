package com.bsdclinic.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class MedicalRecordListResponse {
    private String appointmentId;
    private String medicalRecordId;
    private String patientName;
    private String patientPhone;
    private Timestamp createdAt;
    private String doctorName;
}
