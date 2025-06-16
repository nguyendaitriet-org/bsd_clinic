package com.bsdclinic;

import com.bsdclinic.dto.request.CreateMedicalServiceRequest;

import com.bsdclinic.medical_service.MedicalService;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MedicalServiceMapper {
    MedicalService toEntity(CreateMedicalServiceRequest request);
}
