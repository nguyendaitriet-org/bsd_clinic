package com.bsdclinic.repository;

import com.bsdclinic.ServiceMedicalService;
import com.bsdclinic.medical_service.MedicalService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceMedicalRepository extends JpaRepository<MedicalService, String> {

}