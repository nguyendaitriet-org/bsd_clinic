package com.bsdclinic.invoice;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.bsdclinic.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @Column(name = "invoice_id")
    private String invoiceId;

    @Column(name = "medical_record_id")
    private String medicalRecordId;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "purchased_services", columnDefinition = "jsonb")
    @Type(JsonType.class)
    private List<PurchasedService> purchasedServices;

    @Column(name = "services_total_price")
    private BigDecimal servicesTotalPrice;

    @Column(name = "purchased_medicines", columnDefinition = "jsonb")
    @Type(JsonType.class)
    List<PurchasedMedicine> purchasedMedicines;

    @Column(name = "medicines_total_price")
    private BigDecimal medicinesTotalPrice;

    @Column(name = "advance")
    private BigDecimal advance;

    @Column(name = "grand_total_price")
    private BigDecimal grandTotalPrice;

    @Column(name = "remaining_price")
    private BigDecimal remainingPrice;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        if (invoiceId == null) {
            invoiceId = NanoIdUtils.randomNanoId();
        }
    }
}
