package com.bsdclinic.repository;

import com.bsdclinic.dto.response.IMedicalServiceResponse;
import com.bsdclinic.medical_service.MedicalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicalServiceRepository extends JpaRepository<MedicalService, String>, JpaSpecificationExecutor<MedicalService> {
    @Query(value = "SELECT * FROM medical_services m WHERE vietnamese_text_search(m.title, :keyword)", nativeQuery = true)
    Page<MedicalService> findAllByKeywordWithPage(String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM medical_services m WHERE vietnamese_text_search(m.title, :keyword)", nativeQuery = true)
    List<MedicalService> findAllByKeyword(String keyword);

    @Query("""
            SELECT
                m.medicalServiceId AS medicalServiceId,
                m.title AS title,
                m.price AS price
            FROM ChosenMedicalService AS c
            INNER JOIN MedicalService AS m ON c.medicalServiceId = m.medicalServiceId
            WHERE c.medicalRecordId = :medicalRecordId
    """)
    List<IMedicalServiceResponse> getMedicalServicesByMedicalRecordId(String medicalRecordId);
}