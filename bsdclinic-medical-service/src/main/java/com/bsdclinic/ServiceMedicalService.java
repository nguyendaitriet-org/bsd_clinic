package com.bsdclinic;

import com.bsdclinic.dto.request.MedicalServiceRequest;
import com.bsdclinic.dto.request.MedicalServiceFilter;
import com.bsdclinic.dto.response.IMedicalServiceResponse;
import com.bsdclinic.dto.response.MedicalServiceResponse;
import com.bsdclinic.response.DatatableResponse;

import java.util.List;

public interface ServiceMedicalService {
    void create(MedicalServiceRequest request);
    DatatableResponse getMedicalServices(MedicalServiceFilter medicalServiceFilter);
    List<MedicalServiceResponse> getMedicalServicesForSelection(String keyword);
    List<IMedicalServiceResponse> getMedicalServicesByMedicalRecordId(String medicalRecordId);
    void deleteMedicalService(String medicalServiceId);
    void updateMedicalService(String medicalServiceId, MedicalServiceRequest medicalServiceUpdateRequest);
}
