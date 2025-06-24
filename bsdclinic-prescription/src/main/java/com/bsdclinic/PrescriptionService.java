package com.bsdclinic;

import com.bsdclinic.dto.request.CreatePrescriptionRequest;
import com.bsdclinic.dto.response.CreatePrescriptionResponse;

public interface PrescriptionService {
    CreatePrescriptionResponse createPrescription(CreatePrescriptionRequest request);
}
