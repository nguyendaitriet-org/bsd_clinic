package com.bsdclinic.repository;

import com.bsdclinic.medical_service.MedicalService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalServiceRepository extends JpaRepository<MedicalService, String> {

}