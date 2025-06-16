package com.bsdclinic;

import com.bsdclinic.dto.request.CreateMedicalServiceRequest;
import com.bsdclinic.dto.request.MedicalServiceFilter;
import com.bsdclinic.response.DatatableResponse;

public interface ServiceMedicalService {
    void create(CreateMedicalServiceRequest request);
    DatatableResponse getMedicalServices(MedicalServiceFilter medicalServiceFilter);
}
