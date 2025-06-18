package com.bsdclinic;

import com.bsdclinic.dto.request.MedicalRecordUpdateRequest;
import com.bsdclinic.dto.response.MedicalRecordResponse;

public interface MedicalRecordService {
    MedicalRecordResponse createMedicalRecord(String appointmentId);
    MedicalRecordResponse getMedicalRecord(String medicalRecordId);
    void updateMedicalRecordAndAppointment(String medicalRecordId, String appointmentId, MedicalRecordUpdateRequest request);
}
