package com.bsdclinic.medical_service;

import java.io.Serializable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChosenMedicalServiceId implements Serializable {
    private String medicalRecordId;
    private String medicalServiceId;
}
