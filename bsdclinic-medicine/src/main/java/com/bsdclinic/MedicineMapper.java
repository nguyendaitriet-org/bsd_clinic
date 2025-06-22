package com.bsdclinic;

import com.bsdclinic.dto.request.MedicineRequest;
import com.bsdclinic.dto.response.MedicineResponse;
import com.bsdclinic.medicine.Medicine;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MedicineMapper {
    Medicine toEntity(MedicineRequest medicineRequest);

    MedicineResponse toDto(Medicine medicine);

    List<MedicineResponse> toDtoList(List<Medicine> medicine);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Medicine toEntity(@MappingTarget Medicine medicine, MedicineRequest medicineRequest);
}
