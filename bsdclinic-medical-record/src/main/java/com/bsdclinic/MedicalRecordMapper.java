package com.bsdclinic;

import com.bsdclinic.medical_record.MedicalRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MedicalRecordMapper {
    MedicalRecordDto toDto(MedicalRecord medicalRecord);
}
