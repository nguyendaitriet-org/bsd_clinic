package com.bsdclinic;

import com.bsdclinic.dto.request.CreateServiceMedicalRequest;

import com.bsdclinic.medical_service.MedicalService;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ServiceMedicalMapper {
    MedicalService toEntity(CreateServiceMedicalRequest request);
}
