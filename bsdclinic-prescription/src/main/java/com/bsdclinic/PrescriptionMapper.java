package com.bsdclinic;

import com.bsdclinic.dto.TakenMedicineDto;
import com.bsdclinic.dto.request.CreatePrescriptionRequest;
import com.bsdclinic.dto.response.PrescriptionResponse;
import com.bsdclinic.medicine.TakenMedicine;
import com.bsdclinic.prescription.Prescription;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PrescriptionMapper {
    Prescription toEntity(CreatePrescriptionRequest request);
    TakenMedicine toTakenMedicine(TakenMedicineDto takenMedicineDto);
    PrescriptionResponse toDto(Prescription prescription);
}
