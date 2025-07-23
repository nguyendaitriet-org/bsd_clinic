package com.bsdclinic;

import com.bsdclinic.invoice.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    Invoice findByMedicalRecordId(String medicalRecordId);

    @Modifying
    @Query("UPDATE Invoice i " +
            "SET i.status = :status " +
            "WHERE i.medicalRecordId = :medicalRecordId")
    void updateStatusByMedicalRecordId(String medicalRecordId, String status);

    @Query(value = "SELECT advance FROM medical_records WHERE medical_record_id = :medicalRecordId", nativeQuery = true)
    BigDecimal getAdvanceFromMedicalRecord(String medicalRecordId);
}
