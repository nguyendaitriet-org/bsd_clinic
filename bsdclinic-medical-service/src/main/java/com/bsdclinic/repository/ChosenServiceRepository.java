package com.bsdclinic.repository;

import com.bsdclinic.medical_service.ChosenMedicalService;
import com.bsdclinic.medical_service.ChosenMedicalServiceId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChosenServiceRepository extends JpaRepository<ChosenMedicalService, ChosenMedicalServiceId> {
    void deleteByMedicalRecordId(String medicalRecordId);
}