package com.bsdclinic;

import com.bsdclinic.dto.request.CreateMedicineRequest;
import com.bsdclinic.dto.response.MedicineResponse;
import com.bsdclinic.medicine.Medicine;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MedicineMapper {
    Medicine toEntity(CreateMedicineRequest createMedicineRequest);
    MedicineResponse toDto(Medicine medicine);
    List<MedicineResponse> toDtoList(List<Medicine> medicine);
}
