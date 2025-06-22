package com.bsdclinic;

import com.bsdclinic.dto.request.CreateMedicineRequest;
import com.bsdclinic.medicine.Medicine;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MedicineMapper {
    Medicine toEntity(CreateMedicineRequest createMedicineRequest);
}
