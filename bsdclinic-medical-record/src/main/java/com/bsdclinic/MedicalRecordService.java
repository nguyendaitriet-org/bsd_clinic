package com.bsdclinic;

public interface MedicalRecordService {
    MedicalRecordDto createMedicalRecord(String appointmentId);
    MedicalRecordDto getMedicalRecord(String medicalRecordId);
}
