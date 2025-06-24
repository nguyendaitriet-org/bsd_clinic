package com.bsdclinic.dto.response;

import com.bsdclinic.dto.TakenMedicineDto;
import com.bsdclinic.prescription.ExternalMedicine;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreatePrescriptionResponse {
    private String medicalRecordId;
    private List<ExternalMedicine> externalMedicines;
    private String instruction;
    private String reExamination;
    private List<TakenMedicineDto> takenMedicines;
}
