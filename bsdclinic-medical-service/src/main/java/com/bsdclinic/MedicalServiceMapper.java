package com.bsdclinic;

import com.bsdclinic.dto.request.CreateMedicalServiceRequest;

import com.bsdclinic.dto.response.MedicalServiceResponse;
import com.bsdclinic.medical_service.MedicalService;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MedicalServiceMapper {
    MedicalService toEntity(CreateMedicalServiceRequest request);
    MedicalServiceResponse toDto(MedicalService medicalService);
    List<MedicalServiceResponse> toDtoList(List<MedicalService> medicalService);
}
