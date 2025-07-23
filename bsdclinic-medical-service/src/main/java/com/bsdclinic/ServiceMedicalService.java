package com.bsdclinic;

import com.bsdclinic.dto.request.CreateMedicalServiceRequest;
import com.bsdclinic.dto.request.MedicalServiceFilter;
import com.bsdclinic.dto.request.MedicalServiceUpdateRequest;
import com.bsdclinic.dto.response.IMedicalServiceResponse;
import com.bsdclinic.dto.response.MedicalServiceResponse;
import com.bsdclinic.response.DatatableResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface ServiceMedicalService {
    void create(CreateMedicalServiceRequest request);
    DatatableResponse getMedicalServices(MedicalServiceFilter medicalServiceFilter);
    List<MedicalServiceResponse> getMedicalServicesForSelection(String keyword);
    List<IMedicalServiceResponse> getMedicalServicesByMedicalRecordId(String medicalRecordId);
    void deleteMedicalService(String medicalServiceId);
    void updateMedicalService(String medicalServiceId, MedicalServiceUpdateRequest medicalServiceUpdateRequest);
}
