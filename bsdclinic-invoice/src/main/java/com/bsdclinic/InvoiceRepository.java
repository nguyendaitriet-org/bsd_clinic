package com.bsdclinic;

import com.bsdclinic.invoice.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    Invoice findByMedicalRecordId(String medicalRecordId);
}
