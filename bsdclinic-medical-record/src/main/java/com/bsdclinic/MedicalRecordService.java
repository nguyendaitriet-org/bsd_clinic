package com.bsdclinic;

import com.bsdclinic.dto.request.MedicalRecordFilter;
import com.bsdclinic.dto.request.MedicalRecordUpdateRequest;
import com.bsdclinic.dto.response.MedicalRecordResponse;
import com.bsdclinic.response.DatatableResponse;

public interface MedicalRecordService {
    MedicalRecordResponse createMedicalRecord(String appointmentId);
    DatatableResponse getMedicalRecordsByFilter(MedicalRecordFilter medicalRecordFilter);
    MedicalRecordResponse getMedicalRecord(String medicalRecordId);
    void updateMedicalRecordAndAppointment(String medicalRecordId, String appointmentId, MedicalRecordUpdateRequest request);
    void deleteMedicalRecord(String medicalRecordId, String appointmentId);
}
