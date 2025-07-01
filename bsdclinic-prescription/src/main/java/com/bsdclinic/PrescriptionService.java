package com.bsdclinic;

import com.bsdclinic.dto.request.CreatePrescriptionRequest;
import com.bsdclinic.dto.response.PrescriptionResponse;

public interface PrescriptionService {
    PrescriptionResponse createPrescription(CreatePrescriptionRequest request);
    PrescriptionResponse getPrescription(String prescriptionId);
    void deletePrescription(String prescriptionId);
}
