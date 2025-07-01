package com.bsdclinic.dto.response;

import com.bsdclinic.dto.TakenMedicineDto;
import com.bsdclinic.prescription.ExternalMedicine;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class PrescriptionResponse {
    private String prescriptionId;
    private String medicalRecordId;
    private String patientName;
    private List<ExternalMedicine> externalMedicines;
    private String instruction;
    private String reExamination;
    private List<TakenMedicineDto> takenMedicines;
    private Instant createdAt;
}
