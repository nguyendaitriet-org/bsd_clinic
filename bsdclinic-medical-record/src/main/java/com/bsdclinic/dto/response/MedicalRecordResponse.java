package com.bsdclinic.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MedicalRecordResponse {
    String appointmentId;
    String medicalRecordId;
    private String medicalHistory;
    private String diagnosis;
    private BigDecimal advance;
}
