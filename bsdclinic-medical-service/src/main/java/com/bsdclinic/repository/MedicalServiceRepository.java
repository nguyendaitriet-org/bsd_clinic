package com.bsdclinic.repository;

import com.bsdclinic.medical_service.MedicalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicalServiceRepository extends JpaRepository<MedicalService, String> {
    @Query("SELECT m FROM MedicalService m WHERE " +
            "LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<MedicalService> findAllByKeywordWithPage(String keyword, Pageable pageable);

    @Query("SELECT m FROM MedicalService m WHERE " +
            "LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<MedicalService> findAllByKeyword(String keyword);
}