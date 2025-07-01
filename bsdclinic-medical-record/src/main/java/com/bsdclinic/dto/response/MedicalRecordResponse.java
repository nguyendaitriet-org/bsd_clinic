package com.bsdclinic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordResponse {
    private String appointmentId;
    private String medicalRecordId;
    private String medicalHistory;
    private String diagnosis;
    private BigDecimal advance;
    private String prescriptionId;
    private String invoiceId;
}
