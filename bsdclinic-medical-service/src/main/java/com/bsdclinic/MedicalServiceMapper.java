package com.bsdclinic;

import com.bsdclinic.dto.request.CreateMedicalServiceRequest;

import com.bsdclinic.dto.request.MedicalServiceUpdateRequest;
import com.bsdclinic.dto.response.MedicalServiceResponse;
import com.bsdclinic.medical_service.MedicalService;
import com.bsdclinic.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MedicalServiceMapper {
    MedicalService toEntity(CreateMedicalServiceRequest request);
    MedicalServiceResponse toDto(MedicalService medicalService);
    List<MedicalServiceResponse> toDtoList(List<MedicalService> medicalService);
    MedicalService toEntity(MedicalServiceUpdateRequest request, @MappingTarget MedicalService medicalService);

}
