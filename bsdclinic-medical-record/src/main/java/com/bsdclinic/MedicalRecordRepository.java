package com.bsdclinic;

import com.bsdclinic.medical_record.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, String> {
    boolean existsByMedicalRecordId(String medicalRecordId);
    boolean existsByAppointmentId(String appointmentId);
    boolean existsByAppointmentIdAndMedicalRecordId(String appointmentId, String medicalRecordId);
}
