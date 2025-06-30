package com.bsdclinic.dto.request;

import com.bsdclinic.dto.TakenMedicineDto;
import com.bsdclinic.prescription.ExternalMedicine;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreatePrescriptionRequest {
    private String medicalRecordId;

    @NotBlank(message = "{validation.required.full_name}")
    @Size(max = 255, message = "{validation.input.max_length.255}")
    private String patientName;

    private List<ExternalMedicine> externalMedicines;

    @Size(max = 1000, message = "{validation.input.max_length.1000}")
    private String instruction;

    @Size(max = 1000, message = "{validation.input.max_length.1000}")
    private String reExamination;

    private List<TakenMedicineDto> takenMedicines;
}
