package com.bsdclinic;

import com.bsdclinic.medical_service.MedicalService;
import com.bsdclinic.medicine.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, String> {
    @Query(value = "SELECT * FROM medicines m WHERE vietnamese_text_search(m.title, :keyword)", nativeQuery = true)
    Page<Medicine> findAllByKeywordWithPage(String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM medicines m WHERE vietnamese_text_search(m.title, :keyword)", nativeQuery = true)
    List<Medicine> findAllByKeyword(String keyword);
}
