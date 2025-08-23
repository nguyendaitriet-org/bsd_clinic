package com.bsdclinic;

import com.bsdclinic.dto.request.MedicalServiceRequest;

import com.bsdclinic.dto.response.IMedicalServiceResponse;
import com.bsdclinic.dto.response.MedicalServiceResponse;
import com.bsdclinic.invoice.PurchasedService;
import com.bsdclinic.medical_service.MedicalService;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MedicalServiceMapper {
    MedicalService toEntity(MedicalServiceRequest request);
    MedicalService toEntity(MedicalServiceRequest request, @MappingTarget MedicalService medicalService);
    MedicalServiceResponse toDto(MedicalService medicalService);
    List<MedicalServiceResponse> toDtoList(List<MedicalService> medicalService);
    PurchasedService toPurchasedService(IMedicalServiceResponse medicalServiceResponse);
    List<PurchasedService> toPurchasedServiceList(List<IMedicalServiceResponse> medicalServiceResponse);
}
